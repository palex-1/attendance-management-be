package it.palex.attendanceManagement.commons.messaging.email;

import java.util.Properties;

import it.palex.attendanceManagement.commons.messaging.Email;

public interface EmailSenderService {

	public void sendEmail(Email mail) throws Exception;
	
	
	static void putInProperties(Properties properties, String propName, Object propValue) {
		if(properties==null || propName==null) {
			throw new NullPointerException();
		}
		if(propValue!=null) {
			properties.put(propName, propValue);
		}
	}
	
}
