package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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

import it.palex.attendanceManagement.core.dtos.tasks.CompletionLockAddRequest;
import it.palex.attendanceManagement.core.service.tasks.TaskCompletionLockWebService;
import it.palex.attendanceManagement.data.dto.tasks.TaskCompletionsLocksDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 11 lug 2020
 */
@RestController
@RequestMapping(path="locks")
public class TaskCompletionLocksController extends RestEndpoint {

	@Autowired
	private TaskCompletionLockWebService taskCompletionLockWebService;
	
	
	
	
	@GetMapping(path = "/findAll")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.TASK_COMPLETION_LOCK_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<TaskCompletionsLocksDTO>>> findAll(
			@RequestParam(name = "year", required = false) Integer year,
			@RequestParam(name = "month", required = false) Integer month,
			@RequestParam(name = "status", required = false) String status,
			@PageableDefault(page = 0, size = 10, sort={"year", "month"},direction=Direction.ASC) Pageable pageable) {
	
		GenericResponse<Page<TaskCompletionsLocksDTO>> res = this.taskCompletionLockWebService
				.findAll(month, year, status, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@PostMapping(path = "/create")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.TASK_COMPLETION_LOCK_CREATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TaskCompletionsLocksDTO>> create(
			@RequestBody CompletionLockAddRequest lock){
		
		GenericResponse<TaskCompletionsLocksDTO> res = this.taskCompletionLockWebService.addNewCompletionLock(lock);
		
		return this.buildGenericResponse(res);
	}
	
	
	
	@DeleteMapping(path = "/delete")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.TASK_COMPLETION_LOCK_DELETE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> delete(
			@RequestParam(name = "taskCompletionLockId", required=true) Integer taskCompletionLockId){
		
		GenericResponse<StringDTO> res = this.taskCompletionLockWebService.delete(taskCompletionLockId);
		
		return this.buildGenericResponse(res);
	}
	
	
	@PutMapping(path = "/requestHoursCalculationExecution")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.TASK_COMPLETION_LOCK_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TaskCompletionsLocksDTO>> create(
			@RequestParam(name = "taskCompletionLockId", required=true) Integer taskCompletionLockId){
		
		GenericResponse<TaskCompletionsLocksDTO> res = 
						this.taskCompletionLockWebService.requestHoursCalculationExecution(taskCompletionLockId);
		
		return this.buildGenericResponse(res);
	}
	
	
	
}
