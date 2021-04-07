package it.palex.attendanceManagement.data.entities.enumTypes;

public enum AddressTypeEnum {
	RESIDENCE, DOMICILE;
	
	public static boolean isValid(String value) {
		if(value==null) {
			return false;
		}
		try {
			AddressTypeEnum.valueOf(value);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
}
