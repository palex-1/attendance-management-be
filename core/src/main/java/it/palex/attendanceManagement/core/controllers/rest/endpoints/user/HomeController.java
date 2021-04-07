package it.palex.attendanceManagement.core.controllers.rest.endpoints.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.dtos.userProfile.UserProfileHomeDTO;
import it.palex.attendanceManagement.core.service.userProfile.HomeWebService;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 25 giu 2020
 */
@RestController
@RequestMapping(path="home")
public class HomeController extends RestEndpoint {
		
	@Autowired
	private HomeWebService homeWebService;
	
	
	
	@GetMapping()
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<UserProfileHomeDTO>> getUserCurrentLoggedProfileHome() {
		
		GenericResponse<UserProfileHomeDTO> res = this.homeWebService.getUserCurrentLoggedProfileHome();
		
		return this.buildGenericResponse(res);
	}
	
	
	
	
	
	
	
	
	
	
	
}
