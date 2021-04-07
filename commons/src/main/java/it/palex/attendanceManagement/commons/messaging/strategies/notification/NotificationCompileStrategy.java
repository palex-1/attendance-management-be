package it.palex.attendanceManagement.commons.messaging.strategies.notification;

import org.apache.commons.lang3.StringUtils;

import it.palex.attendanceManagement.commons.messaging.strategies.MessageCompileStrategy;

public interface NotificationCompileStrategy extends MessageCompileStrategy {

	default public String replacePlaceholder(String placeholderKey, String placeholderValue, String text) {
		if(text==null) {
			return null;
		}
		
		String value = placeholderValue==null ? "" : placeholderValue;
		
		return StringUtils.replace(text, placeholderKey, value);
	}
}
