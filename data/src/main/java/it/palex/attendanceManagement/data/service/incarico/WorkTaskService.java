package it.palex.attendanceManagement.data.service.incarico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.dto.tasks.WorkTaskDTO;
import it.palex.attendanceManagement.data.dto.transformers.WorkTaskTransformer;
import it.palex.attendanceManagement.data.entities.QWorkTask;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.ChainPermissions;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.WorkTaskDetailsChainPermissionEvaluator;
import it.palex.attendanceManagement.data.repository.incarico.WorkTaskRepository;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.HttpCodes;

/**
 * @author Alessandro Pagliaro
 *
 */
@Service
public class WorkTaskService implements GenericService {

	private final QWorkTask QWT = QWorkTask.workTask;

	private static final String NOT_FOUND_ERROR = "Cannot found incarico with the specified codiceIncarico";
	
	
	@Autowired
	private WorkTaskRepository workTaskRepo;

	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;

	@Autowired
	private CompletedTaskService completedTaskService;
	
	@Autowired
	private WorkTaskDetailsChainPermissionEvaluator workTaskDetailsChainPermissionEvaluator;
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<WorkTaskDTO> create(WorkTaskDTO task) {
		if (task == null || task.getTaskCode() == null) {
			return this.buildBadDataResponse();
		}

		WorkTask in = this.findByTaskCode(task.getTaskCode());
		if (in != null) {
			return this.buildConflictEntity("There is another incarico with the same codiceIncarico");
		}

		UserProfile currentImpiegato = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();

		if (currentImpiegato == null) {
			return this.buildInternalServerError("Cannot find current logged impiegato");
		}
		
		WorkTask entity = new WorkTask();
		entity.setTaskCode(task.getTaskCode());
		entity.setActivationDate(task.getActivationDate());
		entity.setDeactivationDate(task.getDeactivationDate());
		entity.setTaskDescription(task.getTaskDescription());
		entity.setBillable(task.getBillable());
		entity.setClientVatNum(task.getClientVat());
		entity.setIsEnabledForAllUser(task.getIsEnabledForAllUsers());
		entity.setIsAbsenceTask(task.getIsAbsenceTask());
		
		if (!entity.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse("incarico is not valid");
		}

		entity = this.workTaskRepo.save(entity);

		WorkTaskDTO out = WorkTaskTransformer.mapToDTO(entity);
		out.setCurrentUserCanSeeDetails(true);
		
		return this.buildOkResponse(out);
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<WorkTaskDTO> disattiva(String taskCode) {
		if (taskCode == null) {
			return this.buildBadDataResponse("taskCode cannot be null");
		}
		
		WorkTask incarico = this.findByTaskCode(taskCode);

		if (incarico == null) {
			return this.buildNotFoundResponse(NOT_FOUND_ERROR);
		}
		Date newDataAttivazione = DateUtility.getCurrentDateInUTC();
		
		boolean isValidDataDisattivazioneChange =
				isAdmissibleDataDisattivazioneChange(incarico.getActivationDate(),
						newDataAttivazione);
		if(!isValidDataDisattivazioneChange) {
			return this.buildNotAcceptableResponse(StandardReturnCodesEnum.DEACTIVATION_DATE_CHANGE_INVALID);
		}
		
		isValidDataDisattivazioneChange = isValidDataDisattivazioneChange(
				incarico.getDeactivationDate(), newDataAttivazione);
		
		if(!isValidDataDisattivazioneChange) {
			return this.buildNotAcceptableResponse(StandardReturnCodesEnum.DEACTIVATION_DATE_CHANGE_INVALID_MOVED_NEXT_DEACTIVATION_DATE_ERROR);
		}
		
		incarico.setDeactivationDate(newDataAttivazione);

		incarico = this.workTaskRepo.save(incarico);
		WorkTaskDTO out = WorkTaskTransformer.mapToDTO(incarico);

		return this.buildOkResponse(out, "Incarico Deactivated");
	}

	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST, HttpCodes.NOT_ACCEPTABLE})
	public GenericResponse<WorkTaskDTO> update(WorkTaskDTO task) {
		if (task == null || task.getTaskCode() == null) {
			return this.buildBadDataResponse();
		}
		WorkTask inc = this.findById(task.getId());
	   
		if (inc == null) {
			return this.buildNotFoundResponse(NOT_FOUND_ERROR);
		}

		//if task code is changed
		if(!StringUtils.equalsIgnoreCase(inc.getTaskCode(), task.getTaskCode())) {
			
			WorkTask park = this.findByTaskCode(task.getTaskCode());
			
			if(park!=null) {
				return this.buildConflictEntity("A task with this code already exists");
			}
		}
		
		boolean isValidDataDisattivazioneChange =
				isAdmissibleDataDisattivazioneChange(inc.getActivationDate(),
						task.getDeactivationDate());
		if(!isValidDataDisattivazioneChange) {
			return this.buildNotAcceptableResponse(StandardReturnCodesEnum.DEACTIVATION_DATE_CHANGE_INVALID);
		}
		
		isValidDataDisattivazioneChange = isValidDataDisattivazioneChange(inc.getDeactivationDate(), 
				task.getDeactivationDate());
		
		if(!isValidDataDisattivazioneChange) {
			return this.buildNotAcceptableResponse(StandardReturnCodesEnum.DEACTIVATION_DATE_CHANGE_INVALID_MOVED_NEXT_DEACTIVATION_DATE_ERROR);
		}

		boolean isValidDTO = this.validateUpdateDTO(inc.getActivationDate(), task);
		if (!isValidDTO) {
			return this.buildBadDataResponse();
		}
		inc.setTaskDescription(task.getTaskDescription());
		inc.setBillable(task.getBillable());
		inc.setClientVatNum(task.getClientVat());
		inc.setDeactivationDate(task.getDeactivationDate());
		inc.setTaskCode(task.getTaskCode());
		
		inc = this.workTaskRepo.save(inc);
		WorkTaskDTO out = WorkTaskTransformer.mapToDTO(inc);

		return this.buildOkResponse(out, "Incarico Updated Successfully");
	}

	public WorkTask findById(Long id) {
		if(id==null) {
			return null;
		}
		return this.getFromOptional(this.workTaskRepo.findById(id));
	}

	@Transactional()
	public GenericResponse<StringDTO> delete(String taskCode) {
		if (taskCode == null) {
			return this.buildBadDataResponse();
		}
		WorkTask incarico = this.findByTaskCode(taskCode);
		if (incarico == null) {
			return this.buildNotFoundResponse(NOT_FOUND_ERROR);
		}
		long count = completedTaskService.countIncarichiEseguitiOfIncarico(taskCode);

		if (count > 0) {
			return this.buildNotAcceptableResponse(
					"Cannot delete this Incarico because is used. Delete all incarico eseguito before");
		}

		this.workTaskRepo.delete(incarico);

		return this.buildOkResponse(new StringDTO("Incarico successully deleted"));
	}

	public GenericResponse<Page<WorkTaskDTO>> findAll(Pageable pageable, Predicate predicate,
			String taskDescription, String taskCode) {
		if (pageable == null) {
			return this.buildBadDataResponse();
		}

		UsersAuthDetails user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserAuthDetails();
		
		BooleanBuilder condition = new BooleanBuilder(predicate);
		if (taskCode != null) {
			condition.and(QWT.taskDescription.containsIgnoreCase(taskDescription));
		}
		if (taskCode != null) {
			condition.and(QWT.taskCode.containsIgnoreCase(taskCode));
		}

		Iterator<WorkTask> it = this.workTaskRepo.findAll(condition, pageable).iterator();
		List<WorkTaskDTO> list = new ArrayList<>();
		while (it.hasNext()) {
			WorkTask park = it.next();
			
			Boolean currentUserCanSeeDetails = this.workTaskDetailsChainPermissionEvaluator.checkPermissionWithoutChain(
							user, park.getId(), 
							WorkTaskDetailsChainPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.READ.name());
			
			WorkTaskDTO dto = WorkTaskTransformer.mapToDTO(park);
			dto.setCurrentUserCanSeeDetails(currentUserCanSeeDetails);
			
			list.add(dto);
		}

		long totalCount = this.workTaskRepo.count(condition);

		
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}

	public WorkTask findByTaskCode(String taskCode) {
		if (taskCode == null) {
			throw new NullPointerException("taskCode is null");
		}
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QWT.taskCode.equalsIgnoreCase(taskCode));

		return this.getFirstResultFromIterable(
						this.workTaskRepo.findAll(condition)
					);
	}
	
	/**
	 * Is a valid change if dataDisattivazione is moved next to the older one
	 * @param oldDataAttivazione
	 * @param oldDataDisattivazione
	 * @param newDataDisattivazione
	 * @return
	 */
	private boolean isValidDataDisattivazioneChange(Date oldDataDisattivazione, 
			Date newDataDisattivazione) {
		if(newDataDisattivazione==null) {
			return true; //data disattivazione is moved next  
		}
		if(oldDataDisattivazione==null) {
			return true; //nothing else to check
		}
		if(oldDataDisattivazione.equals(newDataDisattivazione)) {
			return true; //not changed
		}
		if(!oldDataDisattivazione.before(newDataDisattivazione)) {
			return false; //cannot move before
		}
		return true;
	}
	
	private boolean isAdmissibleDataDisattivazioneChange(Date oldDataAttivazione,
			 Date newDataDisattivazione) {
		if(newDataDisattivazione==null) {
			return true; //data disattivazione is moved next  
		}
		if(oldDataAttivazione==null) {
			return false; //cannot set disattivazione without attivazione
		}
		if(!oldDataAttivazione.before(newDataDisattivazione)) {
			return false; //cannot move before
		}
		return true;
	}

	private boolean validateUpdateDTO(Date oldDataAttivazione, WorkTaskDTO task) {
		if (task == null) {
			return false;
		}

		boolean isValid = WorkTask.isValidTaskDescription(task.getTaskDescription())
				&& WorkTask.isValidClientVatNum(task.getClientVat())
				&& WorkTask.isValidBillable(task.getBillable())
				&& WorkTask.isValidDeactivationDate(task.getDeactivationDate());

		isValid = isValid && WorkTask.areAdmissibleActivationDeactivation(
				oldDataAttivazione, task.getDeactivationDate());

		return isValid;
	}

	

	
}
