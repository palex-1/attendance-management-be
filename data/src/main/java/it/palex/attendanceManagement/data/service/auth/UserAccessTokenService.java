package it.palex.attendanceManagement.data.service.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.data.entities.auth.UsersAccessToken;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.auth.UserAccessTokenRepository;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@Service
public class UserAccessTokenService implements GenericService {
	
	
	@Autowired
	private UserAccessTokenRepository userAccessTokenRepo;
	
	
	public UsersAccessToken findByToken(String authToken) {
		return this.userAccessTokenRepo.findByTokenCustom(authToken);
	}


	public void deleteAllTokenOfUser(UsersAuthDetails user) {
		if(user==null) {
			throw new NullPointerException();
		}
		
		this.userAccessTokenRepo.deleteAllTokenOfUserId(user.getId());
	}


	@Transactional
	public UsersAccessToken create(UsersAccessToken accessToken) {
		if(accessToken==null) {
			throw new NullPointerException();
		}
		if(!accessToken.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(accessToken);
		}
		
		return this.userAccessTokenRepo.save(accessToken);
	}


	@Transactional
	public void update(UsersAccessToken accessToken) {
		if(accessToken==null) {
			throw new NullPointerException();
		}
		
		if(!accessToken.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(accessToken);
		}
		this.userAccessTokenRepo.save(accessToken);
	}


	public void delete(UsersAccessToken oldUserAccessToken) {
		if(oldUserAccessToken==null) {
			throw new NullPointerException();
		}
		this.userAccessTokenRepo.delete(oldUserAccessToken);
		
	}




	public int deleteAllExpiredTokens() {
		return this.userAccessTokenRepo.deleteAllTokenExpiredBefore(DateUtility.getCurrentDateInUTC());		
	}
	
	

}
