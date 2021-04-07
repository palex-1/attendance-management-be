package it.palex.attendanceManagement.commons.messaging.firebase;


public class NotificationData {

	private String landingPage;
	private String targetId;
	private String targetSubId;
	
	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTargetSubId() {
		return targetSubId;
	}

	public void setTargetSubId(String targetSubId) {
		this.targetSubId = targetSubId;
	}

	@Override
	public String toString() {
		return "NotificationData [landingPage=" + landingPage + ", targetId=" + targetId + ", targetSubId="
				+ targetSubId + "]";
	}
	
	
}
