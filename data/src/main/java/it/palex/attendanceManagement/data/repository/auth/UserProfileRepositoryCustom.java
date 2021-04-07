package it.palex.attendanceManagement.data.repository.auth;

import it.palex.attendanceManagement.data.entities.UserProfile;

public interface UserProfileRepositoryCustom {

	public UserProfile persistForFirstTime(UserProfile profile);
	
}
