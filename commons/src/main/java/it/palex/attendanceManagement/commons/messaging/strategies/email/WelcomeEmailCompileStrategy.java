package it.palex.attendanceManagement.commons.messaging.strategies.email;

import it.palex.attendanceManagement.commons.messaging.strategies.MessageCompileStrategy;

public class WelcomeEmailCompileStrategy implements MessageCompileStrategy {

	private static final String NAME_PLACEHOLDER = "${name_placeholder}";

	private String name;
	private String message;
	private String subject;
	
	public WelcomeEmailCompileStrategy(String messageText, String name, String subject) {
		if(messageText==null || subject==null) {
			throw new NullPointerException();
		}
		this.message = messageText;
		this.name = name;
		this.subject = subject;
	}
	
	@Override
	public String compileMsg() {
		String mess = this.replacePlaceholder(NAME_PLACEHOLDER, this.name, this.message);
		mess = this.replacePlaceholder(MessageCompileStrategy.SUBJECT_PLACEHOLDER, this.subject, mess);
		
		return mess;
	}
	
	
	
}
