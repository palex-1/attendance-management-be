package it.palex.attendanceManagement.commons.messaging.strategies.email;

import it.palex.attendanceManagement.commons.messaging.strategies.MessageCompileStrategy;

public class EmailOneTimePasswordCompileStrategy implements MessageCompileStrategy {

	private static final String ONE_TIME_PASSWORD_PLACEHOLDER = "${one_time_psw_placeholder}";

	private String message;
	private String oneTimePassword;
	private String subject;

	public EmailOneTimePasswordCompileStrategy(String messageText, String oneTimePassword, String subject) {
		if (messageText == null || oneTimePassword == null || subject==null) {
			throw new NullPointerException();
		}
		this.message = messageText;
		this.oneTimePassword = oneTimePassword;
		this.subject = subject;
	}

	@Override
	public String compileMsg() {
		String mess = this.replacePlaceholder(ONE_TIME_PASSWORD_PLACEHOLDER, this.oneTimePassword, this.message);
		mess = this.replacePlaceholder(MessageCompileStrategy.SUBJECT_PLACEHOLDER, this.subject, mess);
		
	    return mess;
	}

}
