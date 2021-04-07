package it.palex.attendanceManagement.core.controllers.rest.endpoints.turnstile;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.dtos.turnstile.UserAttendanceAddRequest;
import it.palex.attendanceManagement.core.service.turnstile.UserAttendanceWebService;
import it.palex.attendanceManagement.data.dto.core.UserAttendanceDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping(path="attendance")
public class UserAttendanceController extends RestEndpoint {
	
	@Autowired
	private UserAttendanceWebService userAttendanceWebService;
		
	
	@GetMapping(path = "findAll")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_ATTENDANCE_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<UserAttendanceDTO>>> findAll(
			@RequestParam(value="turnstileId", required= true) Long turnstileId,
			@RequestParam(name = "startDate", required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam(name = "endDate", required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
			@PageableDefault(page = 0, size = 10, sort={"timestamp"},direction=Direction.DESC) Pageable pageable) {
		
		Date dateFrom = null;
		
		if(startDate!=null) {
			ZonedDateTime zonedDateTimeFrom = startDate.atZone(ZoneId.systemDefault());
			dateFrom = Date.from(zonedDateTimeFrom.toInstant());
		}
		
		Date dateTo = null;
		
		if(endDate!=null) {
			ZonedDateTime zonedDateTimeTo = endDate.atZone(ZoneId.systemDefault());
	        dateTo = Date.from(zonedDateTimeTo.toInstant());
		}
		
		GenericResponse<Page<UserAttendanceDTO>> res = this.userAttendanceWebService
				.findAllAttendance(dateFrom, dateTo, turnstileId, pageable);
		
		return this.buildGenericResponse(res);
	}

	
	@PostMapping(path="create")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_ATTENDANCE_CREATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<UserAttendanceDTO>> create(
			@RequestBody UserAttendanceAddRequest attendance){
		
		GenericResponse<UserAttendanceDTO> response = this.userAttendanceWebService
				.addNewAttendance(attendance);
					
		return this.buildGenericResponse(response);
	}
	
	
	@DeleteMapping(path="delete")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_ATTENDANCE_DELETE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> delete(
			@RequestParam(value="attendanceId", required= false) Long attendanceId){
		
		GenericResponse<StringDTO> response = this.userAttendanceWebService
				.deleteAttendance(attendanceId);
					
		return this.buildGenericResponse(response);
	}
	
}
