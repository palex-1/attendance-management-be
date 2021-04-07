package it.palex.attendanceManagement.auth.endpoints.auth;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.auth.dto.ChangePasswordDTO;
import it.palex.attendanceManagement.auth.dto.ConfirmPasswordDTO;
import it.palex.attendanceManagement.auth.service.auth.ChangePasswordService;
import it.palex.attendanceManagement.auth.service.auth.ResetPasswordService;
import it.palex.attendanceManagement.commons.security.AuthenticationIntermediateRoles;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.data.service.user.UsersAuthDetailsService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * @author Alessandro Pagliaro
 *
 */
@RestController
@RequestMapping("/authDetails")
public class UserAuthDetailsRC extends RestEndpoint {

	@Autowired
	private ChangePasswordService changePasswordService;

	@Autowired
	private UsersAuthDetailsService userAuthDetails;

	@Autowired
	private ResetPasswordService resetPasswordService;

	
	
	//this type of change password is a change password that is mandatory for login
	@PutMapping(path = "/changePswForLogin")
	@PreAuthorize("hasAuthority('"+AuthenticationIntermediateRoles.USER_MUST_CHANGE_PASSWORD+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> changePswForLogin(
			@RequestBody() ChangePasswordDTO changePsw) {
			GenericResponse<StringDTO> response = this.changePasswordService
					.changePswForLogin(changePsw);
			
			return this.buildGenericResponse(response);
	}
	
	@PutMapping(path = "/changePsw")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> changePassword(
			@RequestBody() ChangePasswordDTO changePsw) {
			GenericResponse<StringDTO> response = this.changePasswordService
					.changePassword(changePsw);
			
			return this.buildGenericResponse(response);
	}

	@PostMapping(path = "/requestChangePassword")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> requestChangePassword(@RequestHeader("user-agent") String userAgent,
			HttpServletRequest httpRequest, @RequestParam(name = "username") String username) {
			GenericResponse<StringDTO> response = this.resetPasswordService
					.resetChangePassword(userAgent, httpRequest, username);

			return this.buildGenericResponse(response);
	}
	
	@PostMapping(path = "/completeChangePassword")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> completeChangePassword(@RequestBody ConfirmPasswordDTO cp) {
			GenericResponse<StringDTO> response = this.resetPasswordService.confirmPassword(cp);

			return this.buildGenericResponse(response);
	}
	
	
}
