package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.service.tasks.UserTaskCoreService;
import it.palex.attendanceManagement.core.service.turnstile.UserAttendanceWebService;
import it.palex.attendanceManagement.data.dto.core.UserAttendanceDTO;
import it.palex.attendanceManagement.data.dto.tasks.CompletedTaskDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping(path="userTask")
public class UserTasksController extends RestEndpoint {

	@Autowired
	private UserTaskCoreService userTaskCoreService;
	
	@Autowired
	private UserAttendanceWebService userAttendanceWebService;
	
	
	@GetMapping(path = "/findCompletedTaskOfMonth")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<CompletedTaskDTO>>> findCompletedTaskOfMonth(
			@RequestParam(name = "year", required = true) Integer year,
			@RequestParam(name = "month", required = true) Integer month,
			@RequestParam(name = "userProfileId", required = true) Integer userProfileId) {
		GenericResponse<List<CompletedTaskDTO>> res = this.userTaskCoreService
				.findCompletedTaskOfMonth(year, month, userProfileId);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path = "/findAllAttendanceOfMonth")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<UserAttendanceDTO>>> findAllAttendanceOfMonth(
			@RequestParam(name = "year", required = true) Integer year,
			@RequestParam(name = "month", required = true) Integer month,
			@RequestParam(name = "userProfileId", required = true) Integer userProfileId) {
		GenericResponse<List<UserAttendanceDTO>> res = this.userAttendanceWebService
				.findAllAttendanceOfMonth(year, month, userProfileId);
		
		return this.buildGenericResponse(res);
	}
	
	
	
}
