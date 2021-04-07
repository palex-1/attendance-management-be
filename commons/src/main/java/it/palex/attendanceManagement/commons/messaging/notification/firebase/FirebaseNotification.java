package it.palex.attendanceManagement.commons.messaging.notification.firebase;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class FirebaseNotification {
	
	private String sound;
	private String title;
	private String body;
	private String icon;
	
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
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Override
	public String toString() {
		return "FirebaseNotification [sound=" + sound + ", title=" + title + ", body=" + body + ", icon=" + icon + "]";
	}
	
}
