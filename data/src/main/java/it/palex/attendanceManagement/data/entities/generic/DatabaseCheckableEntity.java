package it.palex.attendanceManagement.data.entities.generic;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public interface DatabaseCheckableEntity {

	public boolean canBeInsertedInDatabase();
	
	default public String whyCannotBeInsertedInDatabase() {
		return this.toString();
	}
	
}
