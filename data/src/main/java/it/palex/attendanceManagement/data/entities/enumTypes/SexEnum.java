package it.palex.attendanceManagement.data.entities.enumTypes;

/**
 * 
 * @author Alessandro Pagliaro
 *
 * NOTE: THE SIZE OF SexEnum must be 1 char length
 */
public enum SexEnum {
	/**
	 * Male
	 */
	M,
	/**
	 * Female
	 */
	F,
	/**
	 * Other
	 */
	O;
	
	public static boolean isValid(String sex) {
		if(sex==null) {
			return false;
		}
		try {
			SexEnum.valueOf(sex);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
}
