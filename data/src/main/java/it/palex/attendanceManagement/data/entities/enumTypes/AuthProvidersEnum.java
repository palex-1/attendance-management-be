package it.palex.attendanceManagement.data.entities.enumTypes;

public enum AuthProvidersEnum {
	LOCAL;
	
	
	public static boolean isValid(String value) {
		if(value==null) {
			return false;
		}
		try {
			AuthProvidersEnum.valueOf(value);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
}
