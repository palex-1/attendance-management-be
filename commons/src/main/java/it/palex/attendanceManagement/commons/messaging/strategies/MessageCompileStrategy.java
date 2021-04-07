package it.palex.attendanceManagement.commons.messaging.strategies;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

public interface MessageCompileStrategy {

	public static final String SUBJECT_PLACEHOLDER = "${mail_subject}";
	/**
	 * @return the message with all placeholders replaced
	 */
	public String compileMsg();

	
	default public String replacePlaceholder(String placeholderKey, String placeholderValue, String text) {
		if(text==null) {
			return null;
		}
		String value = placeholderValue==null ? "" : HtmlUtils.htmlEscape(placeholderValue);
		
		return StringUtils.replace(text, placeholderKey, value);
	}
}
