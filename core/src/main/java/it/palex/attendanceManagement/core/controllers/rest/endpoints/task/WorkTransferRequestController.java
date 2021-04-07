package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.dtos.userProfile.WorkTransferRequestAdd;
import it.palex.attendanceManagement.core.service.userProfile.WorkTransferRequestWebService;
import it.palex.attendanceManagement.data.dto.core.WorkTransferRequestDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping(path="workTransfer")
public class WorkTransferRequestController extends RestEndpoint {

	@Autowired
	private WorkTransferRequestWebService workTransferRequestWebService;
	
	
	@GetMapping(path="findByDate")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<WorkTransferRequestDTO>> findByDate(
			@RequestParam(name = "day", required = true) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime day) {
		
		ZonedDateTime zonedDateTime = day.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());
        
		GenericResponse<WorkTransferRequestDTO> res = this.workTransferRequestWebService
				.findByDate(date);
		
		return this.buildGenericResponse(res);
	}
	
	
	@PostMapping(path="createOrUpdate")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<WorkTransferRequestDTO>> create(
			@RequestBody WorkTransferRequestAdd addReq) {
		
		GenericResponse<WorkTransferRequestDTO> res = this.workTransferRequestWebService
				.addOrUpdateWorkTransferRequest(addReq);
		
		return this.buildGenericResponse(res);
	}
	
	
	@DeleteMapping(path="delete")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> delete(
			@RequestParam(name = "workTransferReqId", required = true) Long workTransferReqId) {
		
		GenericResponse<StringDTO> res = this.workTransferRequestWebService
				.removeWorkTransferRequest(workTransferReqId);
		
		return this.buildGenericResponse(res);
	}
}
