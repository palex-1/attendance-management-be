package it.palex.attendanceManagement.data.entities.enumTypes;

public enum UserAttendanceTypeEnum {
	ENTER, 
	EXIT;
	
	
	public static boolean isValid(String value) {
		if(value==null) {
			return false;
		}
		try {
			UserAttendanceTypeEnum.valueOf(value);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
}
