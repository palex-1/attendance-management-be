package it.palex.attendanceManagement.core.dtos.tasks;

import it.palex.attendanceManagement.data.dto.core.UserProfileSmallDTO;
import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class WorkTaskSummaryDTO implements DTO {
	
	private static final long serialVersionUID = -6256418644668958253L;
	
	private UserProfileSmallDTO userProfile;
	private Long workedHours;
	
	public UserProfileSmallDTO getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfileSmallDTO userProfile) {
		this.userProfile = userProfile;
	}
	public Long getWorkedHours() {
		return workedHours;
	}
	public void setWorkedHours(Long workedHours) {
		this.workedHours = workedHours;
	}
	
	
}
