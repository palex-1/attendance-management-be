package it.palex.attendanceManagement.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.palex.attendanceManagement.commons.messaging.email.EmailSenderService;
import it.palex.attendanceManagement.commons.messaging.email.GmailEmailSenderService;
import it.palex.attendanceManagement.commons.messaging.notification.FCMNotificationsService;
import it.palex.attendanceManagement.commons.messaging.notification.NotificationSenderService;
import it.palex.attendanceManagement.commons.messaging.sms.SmsSenderService;
import it.palex.attendanceManagement.commons.messaging.sms.TwilioSmsSenderService;

@Configuration
public class MessagingConfiguration {

	@Bean
	public EmailSenderService gmailSenderService() {
		return new GmailEmailSenderService();
	}
	
//	@Bean
//	@Profile({ "dev", "coll", "prod" })
//	public EmailSenderService arubaMailSenderService() {
//		return new ArubaEmailSenderService();
//	}
	
	@Bean
	public SmsSenderService smsSenderService() {
		return new TwilioSmsSenderService();
	}
	
	@Bean
	public NotificationSenderService notificationSenderService() {
		return new FCMNotificationsService(); 
	}
}
