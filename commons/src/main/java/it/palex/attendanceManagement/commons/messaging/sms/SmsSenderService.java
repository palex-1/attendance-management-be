package it.palex.attendanceManagement.commons.messaging.sms;

public interface SmsSenderService {

	public void sendSMS(String sendNumber, String msg) throws Exception;
	
}
