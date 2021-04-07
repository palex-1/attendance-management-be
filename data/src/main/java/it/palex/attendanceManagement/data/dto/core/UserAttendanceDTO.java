package it.palex.attendanceManagement.data.dto.core;

import java.util.Date;

public class UserAttendanceDTO {
	
	private Long id;
	private String type;
	private Date timestamp;
	private TurnstileDTO turnstile;
	private UserProfileSmallDTO userProfile;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public TurnstileDTO getTurnstile() {
		return turnstile;
	}

	public void setTurnstile(TurnstileDTO turnstile) {
		this.turnstile = turnstile;
	}

	public UserProfileSmallDTO getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfileSmallDTO userProfile) {
		this.userProfile = userProfile;
	}

}
