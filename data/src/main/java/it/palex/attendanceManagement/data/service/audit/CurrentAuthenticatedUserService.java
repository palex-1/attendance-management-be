package it.palex.attendanceManagement.data.service.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.data.service.user.UsersAuthDetailsService;

@Service
public class CurrentAuthenticatedUserService {

	@Autowired
	private UsersAuthDetailsService userDetailsSrv;
	
	@Autowired
	private UserProfileService userProfileSrv;
	
	public UserProfile getCurrentAuthenticatedUserProfile() {
		Authentication auth = getCurrentAuthentication();
		if(auth==null) {
			return null;
		}
		return userProfileSrv.findByUsername(auth.getName());
	}
	
	public UsersAuthDetails getCurrentAuthenticatedUserAuthDetails() {
		Authentication auth = getCurrentAuthentication();
		if(auth==null) {
			return null;
		}
		return userDetailsSrv.findByUsername(auth.getName());
	}
	
	public static Authentication getCurrentAuthentication(){
		if(SecurityContextHolder.getContext()!=null) {
			return SecurityContextHolder.getContext().getAuthentication();
		}
		return null;
	}
	
	public static String getCurrentAuthenticatedUserUsername() {
		Authentication auth = getCurrentAuthentication();
		if(auth!=null) {
			return auth.getName();
		}
		return null;
	}

	public boolean currentUserHasAuthority(String authoritiesToHave) {
		if(authoritiesToHave==null) {
			throw new NullPointerException();
		}
		Authentication auth = getCurrentAuthentication();
		if(auth==null) {
			return false;
		}
		
		for(GrantedAuthority authority: auth.getAuthorities()) {
			if(authoritiesToHave.equals(authority.getAuthority())) {
				return true;
			}
		}
		return false;
	}
	
}
