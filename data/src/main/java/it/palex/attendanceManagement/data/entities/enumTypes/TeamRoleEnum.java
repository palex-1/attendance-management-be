package it.palex.attendanceManagement.data.entities.enumTypes;

/**
 * @author Alessandro Pagliaro
 *
 */
public enum TeamRoleEnum {
	PROJECT_MANAGER, 
	DELIVERY_MANAGER, 
	ACCOUNT_MANAGER,
	QA_REVIEWER, 
	RUOLO_GENERICO;
	
	
	public static boolean isValid(String value) {
		if(value==null) {
			return false;
		}
		try {
			TeamRoleEnum.valueOf(value);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
	
	
}
