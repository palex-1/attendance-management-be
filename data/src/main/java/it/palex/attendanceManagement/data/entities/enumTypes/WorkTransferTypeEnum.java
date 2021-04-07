package it.palex.attendanceManagement.data.entities.enumTypes;

public enum WorkTransferTypeEnum {
	NATIONAL, INTERNATIONAL;
	
	
	public static boolean isValid(String type) {
		if(type==null) {
			return false;
		}
		try {
			WorkTransferTypeEnum.valueOf(type);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
	
	
}
