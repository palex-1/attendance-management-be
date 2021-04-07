package it.palex.attendanceManagement.commons.messaging.strategies.sms;

import it.palex.attendanceManagement.commons.messaging.strategies.MessageCompileStrategy;

public class WelcomeSMSCompileStrategy implements MessageCompileStrategy {

	private static final String NAME_PLACEHOLDER = "${name_placeholder}";

	private String name;
	private String message;

	public WelcomeSMSCompileStrategy(String messageText, String name) {
		if (messageText == null) {
			throw new NullPointerException();
		}
		this.message = messageText;
		this.name = name;
	}

	@Override
	public String compileMsg() {
		return this.replacePlaceholder(NAME_PLACEHOLDER, this.name, this.message);
	}

}
