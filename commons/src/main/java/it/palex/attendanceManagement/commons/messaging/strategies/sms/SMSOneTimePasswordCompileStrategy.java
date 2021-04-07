package it.palex.attendanceManagement.commons.messaging.strategies.sms;

import it.palex.attendanceManagement.commons.messaging.strategies.MessageCompileStrategy;

public class SMSOneTimePasswordCompileStrategy implements MessageCompileStrategy {


	private static final String ONE_TIME_PASSWORD_PLACEHOLDER = "${one_time_psw_placeholder}";
	
	private String message;
	private String oneTimePassword;
	
	
	public SMSOneTimePasswordCompileStrategy(String messageText, String oneTimePassword) {
		if(messageText==null || oneTimePassword==null) {
			throw new NullPointerException();
		}
		this.message = messageText;
		this.oneTimePassword = oneTimePassword;
	}
	
	
	@Override
	public String compileMsg() {
		return this.replacePlaceholder(ONE_TIME_PASSWORD_PLACEHOLDER, this.oneTimePassword, this.message);
	}


}
