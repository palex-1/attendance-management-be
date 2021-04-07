package it.palex.attendanceManagement.data.repository.auth;

import java.util.Date;

import it.palex.attendanceManagement.data.entities.auth.UsersAccessToken;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public interface UserAccessTokenRepositoryCustom {

	public UsersAccessToken findByTokenCustom(String token);

	public int deleteAllTokenOfUserId(Integer id);

	public int deleteAllTokenExpiredBefore(Date date);
	
}
