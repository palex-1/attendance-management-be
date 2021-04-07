package it.palex.attendanceManagement.data.repository.auth;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;

public interface CustomUsersAuthDetailsRepository {	
	
	public UsersAuthDetails findByIdCustom(Integer id);
	
	public UsersAuthDetails findByHashedUsername(String username);
	
}
