package it.palex.attendanceManagement.data.entities.enumTypes;

public enum SupportedProvidersEnum {
	FIREBASE;
	
	
	
	public static boolean isValid(String provider) {
		if(provider==null) {
			return false;
		}
		try {
			SupportedProvidersEnum.valueOf(provider);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
}
