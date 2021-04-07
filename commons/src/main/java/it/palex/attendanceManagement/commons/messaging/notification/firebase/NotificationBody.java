package it.palex.attendanceManagement.commons.messaging.notification.firebase;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.palex.attendanceManagement.commons.messaging.firebase.NotificationData;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class NotificationBody {
	
	private String to;
	private String priority;
	private FirebaseNotification notification;
	private NotificationData data;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public FirebaseNotification getNotification() {
		return notification;
	}
	public void setNotification(FirebaseNotification notification) {
		this.notification = notification;
	}
	
	public NotificationData getData() {
		return data;
	}
	public void setData(NotificationData data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "NotificationBody [to=" + to + ", priority=" + priority + ", notification=" + notification + ", data="
				+ data + "]";
	}
	
	
	
}
