package it.palex.attendanceManagement.data.service.user;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.auth.QUserPasswordChangeHistory;
import it.palex.attendanceManagement.data.entities.auth.UserPasswordChangeHistory;
import it.palex.attendanceManagement.data.entities.auth.UserPasswordChangeHistoryPK;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.exceptions.EntityAlreadyExistsException;
import it.palex.attendanceManagement.data.exceptions.EntityCreationException;
import it.palex.attendanceManagement.data.repository.auth.UserPasswordChangeHistoryRepository;
import it.palex.attendanceManagement.library.utils.crypto.sym.EncryptionUtil;

/**
 * @author Alessandro Pagliaro
 *
 */
@Service
public class UserPasswordChangeHistoryService {

private QUserPasswordChangeHistory QUPCH = QUserPasswordChangeHistory.userPasswordChangeHistory;
	
	@Autowired
	private UserPasswordChangeHistoryRepository userPasswordChangeHistoryRepo;
	
	
	
	@Transactional(rollbackFor = Exception.class)
	public void addNewPswChange(String plainTextPassword, UsersAuthDetails user, Date pswChangeDate){
		if(user==null || user.getId()==null){
			throw new EntityCreationException("User is invalid(null) to addNewPswChange");
		}
		String hashedPassword = this.hashPasswordForPswChangeHistory(plainTextPassword);
		
		if(this.existsByKey(hashedPassword, user.getId(), pswChangeDate)){
			throw new EntityAlreadyExistsException("Entity to add already exists in addNewPswChange");
		}
		UserPasswordChangeHistoryPK pk = new UserPasswordChangeHistoryPK();
		pk.setFkIdUsersAuthDetails(user.getId());
		pk.setHashedPassword(hashedPassword);
		pk.setPasswordChangeDate(pswChangeDate);
		UserPasswordChangeHistory entity = new UserPasswordChangeHistory();
		entity.setUserPasswordChangeHistoryPK(pk);
		entity.setUsersAuthDetails(user);
		
		if(!entity.canBeInsertedInDatabase()){
			throw new EntityCreationException("Data to build change psw history are invalid");
		}
		
//		Date beforeDate = DateUtility.addDaysToDate(DateUtility.getCurrentDateInUTC(), this.configSrv.getNumberOfDaysToStoreUserPasswordChangeHistory() * -1);
//		this.userPasswordChangeHistoryRepo.deleteAllChangePasswordOfUserBefore(user.getId(), beforeDate);
		
		this.userPasswordChangeHistoryRepo.save(entity);
	}
	
	
	public boolean existsByKey(String plainTextPassword, Integer fkIdUsersAuthDetails, Date pswChangeDate){
		if(plainTextPassword==null || fkIdUsersAuthDetails==null || pswChangeDate==null){
			throw new NullPointerException();
		}
		BooleanBuilder condition = new BooleanBuilder();
		
		String hashedPassword = this.hashPasswordForPswChangeHistory(plainTextPassword);
		condition.and(QUPCH.userPasswordChangeHistoryPK.fkIdUsersAuthDetails.eq(fkIdUsersAuthDetails))
			.and(QUPCH.userPasswordChangeHistoryPK.hashedPassword.eq(hashedPassword))
			.and(QUPCH.userPasswordChangeHistoryPK.passwordChangeDate.eq(pswChangeDate));
		
		Iterator<UserPasswordChangeHistory> it = this.userPasswordChangeHistoryRepo.findAll(condition).iterator();
		if(it.hasNext()){
			return true;
		}
		return false;
	}

	/*
	 * @param newPassword
	 * @param id
	 * @return
	 */
	public boolean isPasswordRecentlyUsedByUser(String plainTextPassword, Integer fkIdUsersAuthDetails) {
		if(plainTextPassword==null || fkIdUsersAuthDetails==null){
			throw new NullPointerException("plainTextPassword or fkIdUsersAuthDetails cannot be null. fkIdUsersAuthDetails:"+fkIdUsersAuthDetails);//do not add newPassword in exception. If this exception is logged password is plain
		}
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QUPCH.userPasswordChangeHistoryPK.fkIdUsersAuthDetails.eq(fkIdUsersAuthDetails));
		
		String hashedPassword = this.hashPasswordForPswChangeHistory(plainTextPassword);
		condition.and(QUPCH.userPasswordChangeHistoryPK.hashedPassword.eq(hashedPassword));
//		Date afterDate = DateUtility.addDaysToDate(DateUtility.getCurrentDateInUTC(), this.configSrv.getNumberOfDaysToForbitToReusePassword() * -1);
//
//		condition.and(QUPCH.userPasswordChangeHistoryPK.passwordChangeDate.after(afterDate));
		
		return this.userPasswordChangeHistoryRepo.count(condition) > 0;
		
	}	
	
	private String hashPasswordForPswChangeHistory(String plainTextPassword){
		if(plainTextPassword==null){
			throw new NullPointerException();
		}
		return EncryptionUtil.sha256Crypt(plainTextPassword);
	}


	public int deleteAllSuccLoginLogsMadeBefore(Calendar date) {
		if(date==null) {
			return 0;
		}
		
		return this.userPasswordChangeHistoryRepo.deleteAllChangePasswordBefore(date.getTime());
	}
	
	
	
	
}
