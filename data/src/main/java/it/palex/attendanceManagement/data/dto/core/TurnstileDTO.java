package it.palex.attendanceManagement.data.dto.core;

public class TurnstileDTO {
	
	private Long id;
	private String title;
	private String description;
	private String position;
	private Boolean deactivated;
	private String type;
	private String authToken;
	

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Boolean getDeactivated() {
		return deactivated;
	}

	public void setDeactivated(Boolean deactivated) {
		this.deactivated = deactivated;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	

}
