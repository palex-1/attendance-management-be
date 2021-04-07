package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.dtos.tasks.WorkTaskSummaryDTO;
import it.palex.attendanceManagement.core.service.tasks.WorkTaskDetailsWebService;
import it.palex.attendanceManagement.data.dto.tasks.CompletedTaskDTO;
import it.palex.attendanceManagement.data.dto.tasks.IncaricoDetailsOutDTO;
import it.palex.attendanceManagement.data.permissionEvaluators.HasPermission;
import it.palex.attendanceManagement.data.service.incarico.WorkTaskDetailsService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * @author Alessandro Pagliaro
 *
 */
@RestController
@RequestMapping(path="workTaskDetails")
public class WorkTaskDetailsController extends RestEndpoint {

	@Autowired
	private WorkTaskDetailsService incaricoDetailsSrv;
	
	@Autowired
	private WorkTaskDetailsWebService workTaskDetailsWebService;
	
	
	@GetMapping(path="/{taskId}")
	@HasPermission(targetObject = "TASK_DETAILS", permission="READ", identifierParamName= "taskId")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<IncaricoDetailsOutDTO>> getIncarico(
				@PathVariable("taskId") Long taskId){
		GenericResponse<IncaricoDetailsOutDTO> response = 
				this.incaricoDetailsSrv.getDettagliIncarico(taskId);
		
		return this.buildGenericResponse(response);
	}
	
	@GetMapping(path="summary/{taskId}")
	@HasPermission(targetObject = "TASK_DETAILS", permission="READ", identifierParamName= "taskId")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<WorkTaskSummaryDTO>>> getSummaryOfTask(
				@PathVariable("taskId") Long taskId,
				@RequestParam(name = "endDate", required = false) 
				@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
				
				@RequestParam(name = "startDate", required = false) 
				@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
				
				@RequestParam(name = "name", required = false) String name,
				@RequestParam(name = "surname", required = false) String surname,
				@RequestParam(name = "email", required = false) String email,
				@PageableDefault(page = 0, size = 10, sort={"sumOfWorkedHours"}, direction=Direction.DESC) Pageable pageable){
		
		Date dateStart = null;
		
		if(startDate!=null) {
			ZonedDateTime zonedDateTimeFrom = startDate.atZone(ZoneId.systemDefault());
	        dateStart = Date.from(zonedDateTimeFrom.toInstant());
		}
		
		Date dateTo = null;
		
		if(endDate!=null) {
			ZonedDateTime zonedDateTimeTo = endDate.atZone(ZoneId.systemDefault());
	        dateTo = Date.from(zonedDateTimeTo.toInstant());
		}
        
		GenericResponse<Page<WorkTaskSummaryDTO>> response = 
				this.workTaskDetailsWebService.getSummaryOfTask(taskId, dateStart, dateTo, 
						name, surname, email, pageable);
		
		return this.buildGenericResponse(response);
	}
	
	@GetMapping(path="summary/details/{taskId}")
	@HasPermission(targetObject = "TASK_DETAILS", permission="READ", identifierParamName= "taskId")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<CompletedTaskDTO>>> getSummaryDetailsOfTaskAndUser(
			@PathVariable("taskId") Long taskId,
			@RequestParam(name = "endDate", required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
			
			@RequestParam(name = "startDate", required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			
			@RequestParam(name = "userProfileId", required = true) Integer userProfileId,
			@PageableDefault(page = 0, size = 10, sort={"day"}, direction=Direction.DESC) Pageable pageable){
		
		Date dateStart = null;
		
		if(startDate!=null) {
			ZonedDateTime zonedDateTimeFrom = startDate.atZone(ZoneId.systemDefault());
	        dateStart = Date.from(zonedDateTimeFrom.toInstant());
		}
		
		Date dateTo = null;
		
		if(endDate!=null) {
			ZonedDateTime zonedDateTimeTo = endDate.atZone(ZoneId.systemDefault());
	        dateTo = Date.from(zonedDateTimeTo.toInstant());
		}
		
		GenericResponse<Page<CompletedTaskDTO>> response = 
				this.workTaskDetailsWebService.getSummaryDetailsOfTaskAndUser(taskId, userProfileId, 
						dateStart, dateTo, pageable);
		
		return this.buildGenericResponse(response);
		
	}
	
}
