package it.palex.attendanceManagement.core.dtos.turnstile;

import java.util.Date;

public class UserAttendanceAddRequest {

	private String type;
	private Date timestamp;
	private Integer userProfileId;
	private Long turnstileId;


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
	public Integer getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Integer userProfileId) {
		this.userProfileId = userProfileId;
	}

	public Long getTurnstileId() {
		return turnstileId;
	}

	public void setTurnstileId(Long turnstileId) {
		this.turnstileId = turnstileId;
	}
}
