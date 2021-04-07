package it.palex.attendanceManagement.auth.endpoints.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.BooleanDTO;
import it.palex.attendanceManagement.library.utils.HttpCodes;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RequestMapping("checkAuthentication")
@RestController
public class AuthenticationChecker extends RestEndpoint{

	@GetMapping(path="loggedUser")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<BooleanDTO>> imALoggedUser() { //endpoint to check if the user is logged
		try {
			GenericResponse<BooleanDTO> response = new GenericResponse<>(new BooleanDTO(true), HttpCodes.OK, "Success");
			
			return this.buildGenericResponse(response);
		}catch(Exception e) {
			return this.buildInternalErrorResponse(e);
		}
	}
	
}
