package it.palex.attendanceManagement.commons.messaging.firebase;

public class FCMSingleRecipientNotification extends FCMNotificationRequest{
	   private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
