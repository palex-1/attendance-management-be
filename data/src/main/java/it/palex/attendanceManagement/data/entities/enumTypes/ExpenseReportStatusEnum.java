package it.palex.attendanceManagement.data.entities.enumTypes;

public enum ExpenseReportStatusEnum {
	ACCEPTED, 
	PARTIALLY_ACCEPTED,
	REFUSED,
	PROCESSING,
	TO_BE_PROCESSED;
	
	
	public static boolean isValid(String status) {
		if(status==null) {
			return false;
		}
		try {
			ExpenseReportStatusEnum.valueOf(status);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
	
}
