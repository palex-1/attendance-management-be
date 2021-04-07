package it.palex.attendanceManagement.data.entities.enumTypes;

public enum ReportStatusEnum {

	TODO,
	IN_PROGRESS,
	COMPLETED,
	ERROR;

	
	public static boolean isValid(String status) {
		if(status==null) {
			return false;
		}
		try {
			ReportStatusEnum.valueOf(status);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
	
}
