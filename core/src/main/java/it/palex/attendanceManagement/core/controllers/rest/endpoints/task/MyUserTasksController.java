package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.dtos.tasks.CompletedTaskAddDTO;
import it.palex.attendanceManagement.core.dtos.tasks.CompletedTaskUpdateDTO;
import it.palex.attendanceManagement.core.service.tasks.UserTaskCoreService;
import it.palex.attendanceManagement.data.dto.tasks.CompletedTaskDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping(path="myTasks")
public class MyUserTasksController extends RestEndpoint {

	@Autowired
	private UserTaskCoreService userTaskCoreService;
	
	@GetMapping(path = "/findCompletedTaskOfMonth")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<CompletedTaskDTO>>> findCompletedTaskOfMonth(
			@RequestParam(name = "year", required = true) Integer year,
			@RequestParam(name = "month", required = true) Integer month) {
		GenericResponse<List<CompletedTaskDTO>> res = this.userTaskCoreService
				.findCompletedTaskOfMonth(year, month);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path = "/findAll")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<CompletedTaskDTO>>> findAll(
			@RequestParam(name = "day", required = true) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime day,
			@PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.ASC) Pageable pageable) {
		
		ZonedDateTime zonedDateTime = day.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());
        
		GenericResponse<Page<CompletedTaskDTO>> res = this.userTaskCoreService
				.findAllCurrentLoggedUserTasks(date, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@PostMapping(path="create")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<CompletedTaskDTO>> addNewMyTask(
			@RequestBody CompletedTaskAddDTO completedTask) {
		
		GenericResponse<CompletedTaskDTO> res = this.userTaskCoreService
				.addNewTaskOfCurrentLoggedUserTasks(completedTask);
		
		return this.buildGenericResponse(res);
	}
	
	@PutMapping(path="update")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<CompletedTaskDTO>> updateMyTask(
			@RequestBody CompletedTaskUpdateDTO completedTask) {
		
		GenericResponse<CompletedTaskDTO> res = this.userTaskCoreService
				.updateTaskOfCurrentLoggedUserTasks(completedTask);
		
		return this.buildGenericResponse(res);
	}
	
	@DeleteMapping(path="delete")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> deleteMyTask(
			@RequestParam(name = "completedTaskId", required = true) Long completedTaskId) {
		
		GenericResponse<StringDTO> res = this.userTaskCoreService
				.deleteTaskOfCurrentLoggedUserTasks(completedTaskId);
		
		return this.buildGenericResponse(res);
	}
	
}
