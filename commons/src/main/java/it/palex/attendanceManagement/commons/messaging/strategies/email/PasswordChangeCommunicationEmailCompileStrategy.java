package it.palex.attendanceManagement.commons.messaging.strategies.email;

import it.palex.attendanceManagement.commons.messaging.strategies.MessageCompileStrategy;

public class PasswordChangeCommunicationEmailCompileStrategy implements MessageCompileStrategy {

	private static final String MAIL_USER = "${mail_user}";
	private static final String SUPPORT_MAIL_PLACEHOLDER = "${support_mail}";
	
	private String mailUser;
	private String message;
	private String subject;
	private String supportEmail;
	
	public PasswordChangeCommunicationEmailCompileStrategy(String messageText, String subject, 
			String mailUser, String supportEmail) {
		if(messageText==null || subject==null) {
			throw new NullPointerException();
		}
		this.message = messageText;
		this.mailUser = mailUser;
		this.subject = subject;
		this.supportEmail = supportEmail;
	}
	
	@Override
	public String compileMsg() {
		String mess = this.replacePlaceholder(MAIL_USER, this.mailUser, this.message);
		mess = this.replacePlaceholder(MessageCompileStrategy.SUBJECT_PLACEHOLDER, this.subject, mess);		
		mess = this.replacePlaceholder(SUPPORT_MAIL_PLACEHOLDER, this.supportEmail, mess);
		
		return mess;
	}
	
	
	
}

