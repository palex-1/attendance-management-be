package it.palex.attendanceManagement.auth.dto;

public class UpdateUserEmailDTO {

	private Integer userProfileId;
	private String email;
	
	
	public Integer getUserProfileId() {
		return userProfileId;
	}
	public void setUserProfileId(Integer userProfileId) {
		this.userProfileId = userProfileId;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
		
	
	
}
