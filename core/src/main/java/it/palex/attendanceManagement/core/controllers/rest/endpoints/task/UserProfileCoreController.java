package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.service.userProfile.UserProfileWorkTaskCoreService;
import it.palex.attendanceManagement.data.dto.tasks.WorkTaskDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 7 giu 2020
 */
@RestController
@RequestMapping(path="userProfile")
public class UserProfileCoreController extends RestEndpoint {
	
	@Autowired
	private UserProfileWorkTaskCoreService userProfileCoreService;

	
	@GetMapping("allTasksOfUser/{userProfileId}")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<WorkTaskDTO>>> findAll(
			@PathVariable("userProfileId") Integer userProfileId,
			@RequestParam(value="taskDescriptionCustom", required=false) String taskDescription,
			@RequestParam(value="taskCodeCustom", required=false) String taskCode,
			@RequestParam(value="includeDisabledTasks", defaultValue = "true") boolean includeDisabledTasks,
			@PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.ASC) Pageable pageable){
		
		GenericResponse<Page<WorkTaskDTO>> response = this.userProfileCoreService
				.findAllTasksOfUser(userProfileId, taskDescription, taskCode, pageable, includeDisabledTasks);
					
		return this.buildGenericResponse(response);
	}
	
	@GetMapping("allMyTasksEnabled")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<WorkTaskDTO>>> findAllTasksOfCurrentLoggedUser(
				@RequestParam(value="taskDescriptionCustom", required=false) String taskDescription,
				@RequestParam(value="taskCodeCustom", required=false) String taskCode,
				@RequestParam(value="includeDisabledTasks", defaultValue = "true") boolean includeDisabledTasks,
				@PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.ASC) Pageable pageable){
		GenericResponse<Page<WorkTaskDTO>> response = this.userProfileCoreService
				.findAllTasksOfCurrentLoggedUser(taskDescription, taskCode, pageable, includeDisabledTasks);
					
		return this.buildGenericResponse(response);
	}
	
	
}
