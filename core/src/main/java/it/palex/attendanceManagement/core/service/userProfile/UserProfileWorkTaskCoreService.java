package it.palex.attendanceManagement.core.service.userProfile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.dto.tasks.WorkTaskDTO;
import it.palex.attendanceManagement.data.dto.transformers.WorkTaskTransformer;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.data.service.incarico.TeamComponentTaskService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;

@Component
public class UserProfileWorkTaskCoreService implements GenericService {

	@Autowired
	private TeamComponentTaskService teamComponentTaskService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	
	
	//must be call from the user
	public GenericResponse<Page<WorkTaskDTO>> findAllTasksOfCurrentLoggedUser(String taskDescription, String taskCode,
			Pageable pageable, boolean includeDisabled) {
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		return findEnabledTaskOfUser(taskDescription, taskCode, pageable, user, includeDisabled);
	}

	//must be call from a user that has the permission to read users infos
	public GenericResponse<Page<WorkTaskDTO>> findAllTasksOfUser(Integer userProfileId, String taskDescription,
			String taskCode, Pageable pageable, boolean includeDisabled) {
		UserProfile user = this.userProfileService.findById(userProfileId);
		
		if(user==null) {
			return this.buildNotFoundResponse();
		}
		
		return findEnabledTaskOfUser(taskDescription, taskCode, pageable, user, includeDisabled);
	}
	
	private GenericResponse<Page<WorkTaskDTO>> findEnabledTaskOfUser(String taskDescription, String taskCode,
			Pageable pageable, UserProfile user, boolean includeDisabled) {
		List<WorkTask> tasks = this.teamComponentTaskService.findAllWorkTaskOfUser(user, taskDescription, 
				taskCode, pageable, includeDisabled);
		
		List<WorkTaskDTO> list = WorkTaskTransformer.mapToDTO(tasks);
		
		long totalCount = this.teamComponentTaskService.countAllWorkTaskOfUser(user, taskDescription, taskCode, includeDisabled);
		
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}

	
	
	



	
	
	
	
	
}
