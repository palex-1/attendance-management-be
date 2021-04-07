package it.palex.attendanceManagement.commons.messaging.strategies.email;

import it.palex.attendanceManagement.commons.messaging.strategies.MessageCompileStrategy;

public class InvitationEmailCompileStrategy implements MessageCompileStrategy {

	private static final String USERNAME_PLACEHOLDER = "${username}";
	private static final String PASSWORD_PLACEHOLDER = "${password}";
	private static final String FRONTEND_LINK_PLACEHOLDER = "${frontend_link}";
	
	private String username;
	private String password;
	private String message;
	private String subject;
	private String feLink;
	
	
	public InvitationEmailCompileStrategy(String messageText, String subject, String username, 
			String password, String feLink) {
		if(messageText==null || subject==null || username==null || password==null || feLink==null) {
			throw new NullPointerException();
		}
		this.message = messageText;
		this.subject = subject;
		this.password = password;
		this.username = username;
		this.feLink = feLink;
	}
	
	@Override
	public String compileMsg() {
		String mess = this.replacePlaceholder(MessageCompileStrategy.SUBJECT_PLACEHOLDER, this.subject, this.message);		
		mess = this.replacePlaceholder(PASSWORD_PLACEHOLDER, this.password, mess);	
		mess = this.replacePlaceholder(USERNAME_PLACEHOLDER, this.username, mess);	
		mess = this.replacePlaceholder(FRONTEND_LINK_PLACEHOLDER, this.feLink, mess);
		
		return mess;
	}
	
	

	
	
	
}