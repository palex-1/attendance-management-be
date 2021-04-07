package it.palex.attendanceManagement.data.entities.generic;

import javax.persistence.PrePersist; 
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
 
public class DeletableEntityAuditListener {

	@PreUpdate
    private void beforeUpdate(Object obj) {
		if(obj instanceof DeletableEntity) {
			DeletableEntity park = (DeletableEntity) obj;
			
			if(park.getDeleted()==null) {
				park.setDeleted(false);
			}
			
		}
	}
    
    @PreRemove
    private void beforeRemove(Object obj) {
    	//System.out.println("beforeRemove called");
    }
    
    @PrePersist
    private void beforePersist(Object obj) {
    	if(obj instanceof DeletableEntity) {
    		DeletableEntity park = (DeletableEntity) obj;
    		
    		if(park.getDeleted()==null) {
				park.setDeleted(false);
			}
    		
		}
    }
    
}
