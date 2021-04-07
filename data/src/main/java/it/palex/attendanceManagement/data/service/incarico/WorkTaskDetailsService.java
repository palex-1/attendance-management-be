package it.palex.attendanceManagement.data.service.incarico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.data.dto.tasks.IncaricoDetailsOutDTO;
import it.palex.attendanceManagement.data.dto.transformers.WorkTaskTransformer;
import it.palex.attendanceManagement.data.entities.TeamComponentTask;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.enumTypes.TeamRoleEnum;
import it.palex.attendanceManagement.data.service.user.UserContactsService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;


/**
 * @author Alessandro Pagliaro
 *
 */
@Service
public class WorkTaskDetailsService implements GenericService{

	@Autowired
	private WorkTaskService workTaskService;
	
	@Autowired
	private TeamComponentTaskService teamComponentTaskService;
	
	@Autowired
	private UserContactsService userContactsService;
	
	
	
	public GenericResponse<IncaricoDetailsOutDTO> getDettagliIncarico(Long taskId) {
		if(taskId==null) {
			return this.buildBadDataResponse();
		}
		WorkTask task = this.workTaskService.findById(taskId);
		
		if(task==null) {
			return this.buildNotFoundResponse();
		}
		
		UserProfile projectManager = this.getProjectManager(taskId);
		UserProfile deliveryManager = this.getDeliveryManager(taskId);
		UserProfile accountManager = this.getAccountManager(taskId);
		UserProfile qaReviewer = this.getQAReviewer(taskId);
	
		IncaricoDetailsOutDTO res = WorkTaskTransformer.mapToIncaricoDetailsOutDTO(task,
				projectManager, deliveryManager, accountManager, qaReviewer);
		
		return this.buildOkResponse(res);
	}
	
	

	private UserProfile getProjectManager(Long taskId) {
		List<TeamComponentTask> list = findByTaskCodeAndTeamRole(taskId, TeamRoleEnum.PROJECT_MANAGER);
		if(list.isEmpty()) {
			return null;
		}
		UserProfile impiegato = list.get(0).getUserProfile();
		
		return impiegato;
	}
	
	private UserProfile getDeliveryManager(Long taskId) {
		List<TeamComponentTask> list = findByTaskCodeAndTeamRole(taskId, TeamRoleEnum.DELIVERY_MANAGER);
		if(list.isEmpty()) {
			return null;
		}
		UserProfile impiegato = list.get(0).getUserProfile();
		
		return impiegato;
	}
	
	private UserProfile getQAReviewer(Long taskId) {
		List<TeamComponentTask> list = findByTaskCodeAndTeamRole(taskId, TeamRoleEnum.QA_REVIEWER);
		if(list.isEmpty()) {
			return null;
		}		
		UserProfile impiegato = list.get(0).getUserProfile();
		
		return impiegato;
	}

	private UserProfile getAccountManager(Long taskId) {
		List<TeamComponentTask> list = findByTaskCodeAndTeamRole(taskId, TeamRoleEnum.ACCOUNT_MANAGER);
		if(list.isEmpty()) {
			return null;
		}
		UserProfile impiegato = list.get(0).getUserProfile();
		
		return impiegato;
	}
	
	private List<TeamComponentTask> findByTaskCodeAndTeamRole(Long taskId, TeamRoleEnum teamRole){
		return this.teamComponentTaskService.getByIncaricoImpiegatiByRole(taskId, teamRole);
	}
	
	
	
	
}
