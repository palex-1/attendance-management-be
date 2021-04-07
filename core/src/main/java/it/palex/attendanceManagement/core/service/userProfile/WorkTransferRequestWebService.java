package it.palex.attendanceManagement.core.service.userProfile;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.core.dtos.userProfile.WorkTransferRequestAdd;
import it.palex.attendanceManagement.data.dto.core.WorkTransferRequestDTO;
import it.palex.attendanceManagement.data.dto.transformers.core.WorkTransferRequestTransformer;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.WorkTransferRequest;
import it.palex.attendanceManagement.data.entities.enumTypes.WorkTransferTypeEnum;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.core.TaskCompletionsLocksService;
import it.palex.attendanceManagement.data.service.core.WorkTransferRequestService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Service
public class WorkTransferRequestWebService implements GenericService {

	@Autowired
	private WorkTransferRequestService workTransferRequestService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
		
	@Autowired
	private TaskCompletionsLocksService taskCompletionsLocksService;
	
	
	
	public GenericResponse<WorkTransferRequestDTO> findByDate(Date day) {
		if(day==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		WorkTransferRequest req = this.workTransferRequestService.findByUserAndDay(user, day);
		
		return this.buildOkResponse(WorkTransferRequestTransformer.mapToDTO(req));
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<WorkTransferRequestDTO> addOrUpdateWorkTransferRequest(WorkTransferRequestAdd addReq){
		if(addReq==null || addReq.getDay()==null || addReq.getType()==null || 
				!WorkTransferTypeEnum.isValid(addReq.getType())) {
			return this.buildBadDataResponse();
		}
		
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		Calendar date = DateUtility.dateToCalendar(addReq.getDay());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		if(user.getDateOfEmployment()!=null) {
			if(date.before(user.getDateOfEmployment())) {
				return this.buildUnprocessableEntity(StandardReturnCodesEnum.USER_IS_NOT_AN_EMPLOYEE);
			}
		}
		
		WorkTransferRequest oldReq = this.workTransferRequestService.findByUserAndDay(user, addReq.getDay());
		
		if(oldReq!=null) {
			oldReq.setType(WorkTransferTypeEnum.valueOf(addReq.getType()).name());
			oldReq = this.workTransferRequestService.saveOrUpdate(oldReq);
			
			return this.buildOkResponse(WorkTransferRequestTransformer.mapToDTO(oldReq), StandardReturnCodesEnum.UPDATED_WORK_TRANSFER_REQUEST);
		}
		
		WorkTransferRequest toAdd = new WorkTransferRequest();
		toAdd.setUserProfile(user);
		toAdd.setType(WorkTransferTypeEnum.valueOf(addReq.getType()).name());
		toAdd.setDay(addReq.getDay());
		
		toAdd = this.workTransferRequestService.saveOrUpdate(toAdd);
		
		return this.buildOkResponse(WorkTransferRequestTransformer.mapToDTO(toAdd));
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> removeWorkTransferRequest(Long workTransferRequestId){
		if(workTransferRequestId==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		WorkTransferRequest toDelete = this.workTransferRequestService.findByIdAndUser(workTransferRequestId, user);
		
		if(toDelete==null) {
			return this.buildStringOkResponse("Already deleted"); //do not give error
		}
		
		Calendar date = DateUtility.dateToCalendar(toDelete.getDay());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		this.workTransferRequestService.delete(toDelete);
		
		return this.buildStringOkResponse("Successfully deleted");
	}
	
	
}
