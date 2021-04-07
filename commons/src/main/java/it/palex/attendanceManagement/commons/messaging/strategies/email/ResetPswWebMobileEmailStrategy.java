package it.palex.attendanceManagement.commons.messaging.strategies.email;

import it.palex.attendanceManagement.commons.messaging.strategies.MessageCompileStrategy;

public class ResetPswWebMobileEmailStrategy implements MessageCompileStrategy {

	private static final String RESET_LINK_WEB_APP = "${reset_psw_link}";
	
	private String resetLinkWebApp;
	private String message;
	private String subject;
	
	public ResetPswWebMobileEmailStrategy(String messageText, String resetLinkWebApp, 
			String subject) {
		if(messageText==null || subject==null) {
			throw new NullPointerException();
		}
		this.message = messageText;
		this.resetLinkWebApp = resetLinkWebApp;
		this.subject = subject;
	}
	
	@Override
	public String compileMsg() {
		String message = this.replacePlaceholder(RESET_LINK_WEB_APP, this.resetLinkWebApp, this.message);
		message = this.replacePlaceholder(MessageCompileStrategy.SUBJECT_PLACEHOLDER, this.subject, message);
		
		return message;
	}
		
		
}
