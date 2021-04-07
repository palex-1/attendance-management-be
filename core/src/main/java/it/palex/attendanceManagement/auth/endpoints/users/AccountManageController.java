package it.palex.attendanceManagement.auth.endpoints.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.auth.service.users.UserProfileWebService;
import it.palex.attendanceManagement.data.dto.core.UserProfileDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping("/manageAccounts")
public class AccountManageController  extends RestEndpoint {

	@Autowired
	private UserProfileWebService userProfileWebService;
	
	
	@PutMapping("disableProfile")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileDTO>> lockUserProfile(
    		@RequestParam(name="userProfileId", required=true) Integer userProfileId){
	     
    	GenericResponse<UserProfileDTO>	res = this.userProfileWebService
    			.disableUserProfile(userProfileId);
    	
    	return this.buildGenericResponse(res);	
	}
	
	@PutMapping("enableProfile")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileDTO>> unlockProfile(
    		@RequestParam(name="userProfileId", required=true) Integer userProfileId){
	     
    	GenericResponse<UserProfileDTO>	res = this.userProfileWebService
    			.enableUserProfile(userProfileId);
    	
    	return this.buildGenericResponse(res);	
	}
	
}
