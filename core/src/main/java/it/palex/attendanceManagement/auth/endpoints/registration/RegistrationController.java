package it.palex.attendanceManagement.auth.endpoints.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.auth.dto.UserProfileCreationRequestDTO;
import it.palex.attendanceManagement.auth.service.registration.RegistrationWebService;
import it.palex.attendanceManagement.data.dto.core.UserProfileDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;


@RestController
@RequestMapping("/registration")
public class RegistrationController extends RestEndpoint {

	@Autowired
	private RegistrationWebService registrationWebService;
	
	@PostMapping(path = "/insertUser")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_CREATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<UserProfileDTO>> insertUser(
			@RequestBody UserProfileCreationRequestDTO toAdd) {
		GenericResponse<UserProfileDTO> res = this.registrationWebService.createNewUser(toAdd);
		
		return this.buildGenericResponse(res);
	}
	

	
	
}
