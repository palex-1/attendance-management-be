package it.palex.attendanceManagement.commons.messaging.firebase;

public class FCMTopicNotification extends FCMNotificationRequest {
	
	private String topic;
	
	public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
