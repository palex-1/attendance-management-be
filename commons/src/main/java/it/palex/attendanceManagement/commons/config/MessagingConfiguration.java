package it.palex.attendanceManagement.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.palex.attendanceManagement.commons.messaging.notification.FCMNotificationsService;
import it.palex.attendanceManagement.commons.messaging.notification.NotificationSenderService;
import it.palex.attendanceManagement.commons.messaging.sms.SmsSenderService;
import it.palex.attendanceManagement.commons.messaging.sms.TwilioSmsSenderService;

@Configuration
public class MessagingConfiguration {

	@Bean
	public SmsSenderService smsSenderService() {
		return new TwilioSmsSenderService();
	}
	
	@Bean
	public NotificationSenderService notificationSenderService() {
		return new FCMNotificationsService(); 
	}

}
