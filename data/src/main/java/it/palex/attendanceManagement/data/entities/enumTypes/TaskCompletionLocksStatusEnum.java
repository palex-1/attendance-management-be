package it.palex.attendanceManagement.data.entities.enumTypes;

public enum TaskCompletionLocksStatusEnum {
	NOT_TO_BE_PROCESSED, 
	TO_BE_PROCESSED, 
	PROCESSING, 
	PROCESSED;
	
	
	
	public static boolean isValid(String status) {
		if(status==null) {
			return false;
		}
		try {
			TaskCompletionLocksStatusEnum.valueOf(status);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
	
	
}
