package it.palex.attendanceManagement.core.dtos.turnstile;

public class UserAttendanceAddSwitchedRequest {

	private Integer userProfileId;
	private Long turnstileId;


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
