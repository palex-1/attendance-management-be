package it.palex.attendanceManagement.data.entities.generic;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;

/**
 * @author Alessandro Pagliaro
 *
 */
public class CheckableInsertAuditListener {

	
    @PreUpdate
    private void beforeUpdate(Object obj) {
		if(obj instanceof DatabaseCheckableEntity) {
			DatabaseCheckableEntity park = (DatabaseCheckableEntity) obj;
			if(!park.canBeInsertedInDatabase()) {
				throw new DataCannotBeInsertedInDatabase(park);
			}
		}
	}
    
    @PreRemove
    private void beforeRemove(Object obj) {
    	//System.out.println("beforeRemove called");
    }
    
    @PrePersist
    private void beforePersist(Object obj) {
    	if(obj instanceof DatabaseCheckableEntity) {
			DatabaseCheckableEntity park = (DatabaseCheckableEntity) obj;
			if(!park.canBeInsertedInDatabase()) {
				throw new DataCannotBeInsertedInDatabase(park);
			}
		}
    }
    
    
}
