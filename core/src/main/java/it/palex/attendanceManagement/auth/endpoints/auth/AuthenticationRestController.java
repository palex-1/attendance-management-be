package it.palex.attendanceManagement.auth.endpoints.auth;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.auth.dto.AuthenticationDTO;
import it.palex.attendanceManagement.auth.dto.AuthoritiesDTO;
import it.palex.attendanceManagement.auth.dto.CompleteAuthenticationDTO;
import it.palex.attendanceManagement.auth.service.auth.AuthenticationService;
import it.palex.attendanceManagement.commons.security.AuthenticationIntermediateRoles;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping("/authentication")
public class AuthenticationRestController extends RestEndpoint{
	
	@Value("${security.auth.header}")
    private String tokenHeader;
	
	
	@Autowired
	private AuthenticationService authService;
	
   
    @PostMapping()
    @PermitAll
    public ResponseEntity<GenericResponse<StringDTO>> createAuthenticationTokenUrlEncoded(
    			   @RequestHeader("user-agent") String userAgent, 
    			   @RequestBody AuthenticationDTO auth,
		    	   HttpServletRequest httpRequest,
		    	   HttpServletResponse httpServletResponse){
    	
    	try{
    		GenericResponse<StringDTO> response = authService.authenticate(auth, userAgent, httpRequest);
    		
    		if(response.getCode()!=HttpStatus.OK.value()){
    			return this.buildGenericResponse(response);
    		}
    		final String token = response.getData().getValue();
    		    		
    		ResponseEntity<GenericResponse<StringDTO>> park = ResponseEntity.ok(response);
    		httpServletResponse.addHeader(tokenHeader, "Bearer "+token+"");
    		
    		return park;
    		
    	}catch(Exception e){
    		return this.buildInternalErrorResponse(e);
    	}
    	
    }


    @GetMapping(path="/myAuthorities")
    @PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	public ResponseEntity<GenericResponse<AuthoritiesDTO>> getMyAuthorities(){
		GenericResponse<AuthoritiesDTO> response = authService.getUserAuthorityList();
			
		return this.buildGenericResponse(response);
	}
    

    @PostMapping(path = "/complete")
	@PreAuthorize("hasAuthority('"+AuthenticationIntermediateRoles.TWO_FA_IN_PROGRESS+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> completeTwoFAAuthentication(
			@RequestBody CompleteAuthenticationDTO auth) {

		return this.buildGenericResponse(this.authService.completeAuthentication(auth));
	}
}
