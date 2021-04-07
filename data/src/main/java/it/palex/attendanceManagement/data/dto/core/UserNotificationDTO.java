package it.palex.attendanceManagement.data.dto.core;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class UserNotificationDTO implements DTO {

	private static final long serialVersionUID = 6762291611853110923L;

	private Long id;
    private String title;
    private String text;
    private String landingPage;
    private String targetId;
    private String targetSubId;
    
    
    
	public String getTargetSubId() {
		return targetSubId;
	}
	public void setTargetSubId(String targetSubId) {
		this.targetSubId = targetSubId;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
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
	
}
