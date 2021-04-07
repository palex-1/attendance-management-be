package it.palex.attendanceManagement.auth.dto;

import it.palex.attendanceManagement.data.dto.core.UserProfileAddressDTO;

public class UserProfileAddressUpdateRequest extends UserProfileAddressDTO {

	private Integer userProfileId;

	public Integer getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Integer userProfileId) {
		this.userProfileId = userProfileId;
	}
	
}
