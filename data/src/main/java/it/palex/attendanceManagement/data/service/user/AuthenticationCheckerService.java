package it.palex.attendanceManagement.data.service.user;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.BooleanDTO;
import it.palex.attendanceManagement.library.service.GenericService;

@Service
public class AuthenticationCheckerService implements GenericService {

	
	public GenericResponse<BooleanDTO> hasUserAuthority(String authority){
		if(authority==null) {
			return this.buildBadDataResponse();
		}
		if(SecurityContextHolder.getContext()==null || SecurityContextHolder.getContext().getAuthentication()==null 
				|| SecurityContextHolder.getContext().getAuthentication().getAuthorities()==null) {
			return this.buildOkResponse(new BooleanDTO(false));
		}
		Collection<? extends GrantedAuthority> granthedAuth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for(GrantedAuthority auth: granthedAuth) {
			if(StringUtils.equals(auth.getAuthority(), authority)){
				return this.buildOkResponse(new BooleanDTO(true));
			}
		}
		
		return this.buildOkResponse(new BooleanDTO(false));
	}
	
	
}
