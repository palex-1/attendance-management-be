package it.palex.attendanceManagement.data.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.auth.QUsersAuthDetails;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.auth.UsersAuthDetailsRepository;
import it.palex.attendanceManagement.library.service.GenericService;

@Service
public class UsersAuthDetailsService implements GenericService{

private final QUsersAuthDetails QUAD = QUsersAuthDetails.usersAuthDetails;
	
	@Autowired
	private UsersAuthDetailsRepository userAuthDetailsRepo;
	
	
	public UsersAuthDetails getByID(Integer userId) {
		return this.userAuthDetailsRepo.findByIdCustom(userId);
	}
	
	/**
	 * THE USERNAME IS IGNORE CASE
	 * @param username
	 * @return if exists, UsersAuthDetails with the specified username otherwise null
	 */
	public UsersAuthDetails findByUsername(String username) {
		if(username==null) {
			return null;
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUAD.username.equalsIgnoreCase(username));
		
		return this.getFirstResultFromIterable(this.userAuthDetailsRepo.findAll(cond));
	}

	public UsersAuthDetails saveOrUpdate(UsersAuthDetails newUser) {
		if(newUser==null) {
			throw new NullPointerException();
		}
		if(!newUser.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(newUser);
		}
		
		return this.userAuthDetailsRepo.save(newUser);
	}
	
	
	public UsersAuthDetails update(UsersAuthDetails user) {
		if(user==null) {
			throw new NullPointerException();
		}
		if(!user.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(user);
		}
		
		return this.userAuthDetailsRepo.save(user);
	}


}