package it.palex.attendanceManagement.data.entities.generic;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import it.palex.attendanceManagement.library.utils.DateUtility;

/**
 * @author Alessandro Pagliaro
 *
 */
public class BasicAuditListener {

	public static final String NO_USER = "NO_USER";
	
    @PreUpdate
    private void beforeUpdate(Object obj) {
		if(obj instanceof AuditableEntity) {
			AuditableEntity park = (AuditableEntity) obj;
			park.setLastModifiedBy(this.getCurrentLoggedInUsername());
			park.setLastModifiedDate(DateUtility.getCurrentDateInUTC());
		}
	}
    
    @PreRemove
    private void beforeRemove(Object obj) {
    	//System.out.println("beforeRemove called");
    }
    
    @PrePersist
    private void beforePersist(Object obj) {
    	if(obj instanceof AuditableEntity) {
			AuditableEntity park = (AuditableEntity) obj;
			park.setCreatedBy(this.getCurrentLoggedInUsername());
			park.setCreatedDate(DateUtility.getCurrentDateInUTC());
			park.setLastModifiedBy(this.getCurrentLoggedInUsername());
			park.setLastModifiedDate(DateUtility.getCurrentDateInUTC());
		}
    }
    
    
    private String getCurrentLoggedInUsername() {
    	String username = this.getCurrentAuthenticatedUserUsername();
    	if(username==null) {
    		return NO_USER;
    	}
    	return username;
    }
    
    private Authentication getCurrentAuthentication(){
		if(SecurityContextHolder.getContext()!=null) {
			return SecurityContextHolder.getContext().getAuthentication();
		}
		return null;
	}
	
	private String getCurrentAuthenticatedUserUsername() {
		Authentication auth = getCurrentAuthentication();
		if(auth!=null) {
			return auth.getName();
		}
		return null;
	}
	
}