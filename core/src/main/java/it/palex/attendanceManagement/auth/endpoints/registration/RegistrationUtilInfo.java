package it.palex.attendanceManagement.auth.endpoints.registration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.auth.dto.UserProfileCreationRequestDTO;
import it.palex.attendanceManagement.auth.service.registration.RegistrationWebService;
import it.palex.attendanceManagement.data.dto.auth.PermissionGroupLabelDTO;
import it.palex.attendanceManagement.data.dto.core.CompanyDTO;
import it.palex.attendanceManagement.data.dto.core.UserLevelDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping("/registrationUtil")
public class RegistrationUtilInfo extends RestEndpoint {

	@Autowired
	private RegistrationWebService registrationWebService;
	
	
	
	@GetMapping(path = "/findAllPermissionGroupLabels")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<PermissionGroupLabelDTO>>> findAllPermissionGroupLabels() {
		GenericResponse<List<PermissionGroupLabelDTO>> res = this.registrationWebService
				.findAllPermissionGroupLabels();
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path = "/findAllCompanies")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<CompanyDTO>>> findAllCompanies() {
		GenericResponse<List<CompanyDTO>> res = this.registrationWebService
				.findAllCompanies();
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path = "/findAllUserLevels")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<UserLevelDTO>>> findAllUserLevels() {
		GenericResponse<List<UserLevelDTO>> res = this.registrationWebService
				.findAllUserLevels();
		
		return this.buildGenericResponse(res);
	}
	
}
