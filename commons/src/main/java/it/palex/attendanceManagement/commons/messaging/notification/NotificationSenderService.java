package it.palex.attendanceManagement.commons.messaging.notification;

import it.palex.attendanceManagement.commons.messaging.firebase.FCMNotificationRequest;

public interface NotificationSenderService {

	public boolean sendNotification(FCMNotificationRequest notification) throws Exception;
	
}
