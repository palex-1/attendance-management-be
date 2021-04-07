package it.palex.attendanceManagement.data.entities.enumTypes;

/**
 * 
 * @author Alessandro Pagliaro
 *
 * NOTE: THE MAX LENGHT OF THE ENUM IS 30 chars!
 */
public enum ContactTypeEnum {

	PHONE_NUMBER, 
	PHONE_NUMBER_REPLACEMENT, //phone number to replace PHONE_NUMBER when confirmed
	EMAIL_ADDRESS;

	public static boolean isValid(String cType) {
		if(cType==null) {
			return false;
		}
		try {
			ContactTypeEnum.valueOf(cType);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
	
}
