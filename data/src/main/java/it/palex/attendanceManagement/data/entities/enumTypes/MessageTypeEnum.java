package it.palex.attendanceManagement.data.entities.enumTypes;

public enum MessageTypeEnum {
	WELCOME_EMAIL,
	BASIC_EMAIL,
	ONE_TIME_PSW_EMAIL,
	
	WELCOME_MSG, 
	ONE_TIME_PSW_SMS,
	RESET_PSW_EMAIL,
	PASSWORD_CHANGE_COMMUNICATION_EMAIL,
	USER_INVITED_TO_APP,
	
	REPORT_PROBLEM;

	
	
	
	public static boolean isValid(String messageType) {
		if(messageType==null) {
			return false;
		}
		try {
			MessageTypeEnum.valueOf(messageType);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
}
