package it.palex.attendanceManagement.core.controllers.rest.endpoints.turnstile;

import it.palex.attendanceManagement.core.dtos.turnstile.UserAttendanceTurnstileAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.dtos.turnstile.NameSurnameIdUsersDTO;
import it.palex.attendanceManagement.core.service.turnstile.TurnstileAttendanceWebService;
import it.palex.attendanceManagement.data.dto.core.UserAttendanceDTO;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;

@RestController
@RequestMapping(path="turnstile-attendance")
public class TurnstileAttendanceController  extends RestEndpoint {

	@Autowired
	private TurnstileAttendanceWebService turnstileAttendanceService;
	
	@GetMapping(value = "/find-all-user-name-id")
	public ResponseEntity<GenericResponse<NameSurnameIdUsersDTO>> findAllUserNameAndId(
			@RequestParam(name="authToken", required = true) String turnstileToken){
				
		GenericResponse<NameSurnameIdUsersDTO> response = this.turnstileAttendanceService
				.findAllUserNameAndId(turnstileToken);
					
		return this.buildGenericResponse(response);
	}
	
	
	@PostMapping(value = "/register-attendance")
	public ResponseEntity<GenericResponse<UserAttendanceDTO>> registerAttendance(
			@RequestParam(name="authToken", required = true) String turnstileToken,
			@RequestBody UserAttendanceTurnstileAddRequest attendance){
				
		GenericResponse<UserAttendanceDTO> response = this.turnstileAttendanceService
				.addNewAttendance(turnstileToken, attendance);
					
		return this.buildGenericResponse(response);
	}
	
	
	
	
	
}
