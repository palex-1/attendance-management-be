package it.palex.attendanceManagement.commons.messaging.firebase;

public abstract class FCMNotificationRequest {

	private String title;
	private String message;
	private String priority;
	private String icon;
	private String body;
	private NotificationData data;
	private String sound;
	
	
	
	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public NotificationData getData() {
		return data;
	}

	public void setData(NotificationData data) {
		this.data = data;
	}
}
