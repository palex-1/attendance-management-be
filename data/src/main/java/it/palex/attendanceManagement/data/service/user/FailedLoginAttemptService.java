package it.palex.attendanceManagement.data.service.user;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.data.entities.auth.FailedLoginAttempt;
import it.palex.attendanceManagement.data.exceptions.EntityAlreadyExistsException;
import it.palex.attendanceManagement.data.exceptions.EntityCreationException;
import it.palex.attendanceManagement.data.repository.failedLoginAttempt.FailedLoginAttemptRepository;

/**
 * @author Alessandro Pagliaro
 *
 */
@Service
public class FailedLoginAttemptService {

	@Autowired
	private FailedLoginAttemptRepository failedLoginAttemptRepo;

	/**
	 * @param ip
	 * @param from
	 * @param to
	 * @return
	 */
	public long getCountOfAllFailedLoginAttemptOfIpInTimeRange(String ip, Calendar from,
			Calendar to) {
		if(ip==null || from==null || to==null){
			throw new NullPointerException("ip:"+ip+" from:"+from+" to:"+to);
		}
		return failedLoginAttemptRepo.getCountOfAllFailedLoginAttemptOfIpInTimeRange(ip, from, to);
	}

	/**
	 * @param ip
	 * @param loginDate
	 * @param username
	 * @return
	 */
	public boolean existsAnAttemptMadeByIpForUsernameInDate(String ip, Date loginDate, String username) {
		if(ip==null || loginDate==null || username==null){
			throw new NullPointerException(" ip:"+ip+" loginDate:"+loginDate+" username:"+username);
		}
		return 	failedLoginAttemptRepo.existsAnAttemptMadeByIpForUsernameInDate(ip, loginDate, username);
	}

	/**
	 * @param ip
	 * @param username
	 * @param from
	 * @param actualDateInUTC
	 * @return
	 */
	public long getCountOfAllFailedLoginAttemptOfIpAndUsernameInRange(String ip, String username, Calendar from,
			Calendar to) {
		if(ip==null || username==null || from==null || to==null){
			throw new NullPointerException("ip:"+ip+" username:"+username+" from:"+from+" to:"+to);
		}
		return failedLoginAttemptRepo.getCountOfAllFailedLoginAttemptOfIpAndUsernameInRange(ip, username, from, to);
	}

	/**
	 * @param ip
	 * @param userAgent
	 * @param username
	 * @param currentDate
	 * @return
	 */
	public FailedLoginAttempt findByKey(String ip, String userAgent, String username, Date currentDate) {
		if(ip==null || userAgent==null || username==null || currentDate==null){
			throw new NullPointerException("ip:"+ip+" userAgent:"+userAgent+" username:"+username+" currentDate:"+currentDate);
		}
		
		return this.failedLoginAttemptRepo.getByKey(ip, userAgent, currentDate, username);
	}

	/**
	 * @param attempt
	 * @throws EntityCreationException 
	 * @throws EntityAlreadyExistsException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void create(FailedLoginAttempt attempt) throws EntityCreationException, EntityAlreadyExistsException {
		if(attempt==null){
			throw new NullPointerException("Cannot add a null FailedLoginAttempt");
		}
		if(!attempt.canBeInsertedInDatabase()){
			throw new EntityCreationException("The entity cannot be added in database");
		}
		String ip = attempt.getFailedLoginAttemptPK().getIp();
		String userAgent =  attempt.getFailedLoginAttemptPK().getUserAgent();
		String username =  attempt.getFailedLoginAttemptPK().getUsername();
		Date loginDate = attempt.getFailedLoginAttemptPK().getLoginDate();
		
		FailedLoginAttempt park = this.findByKey(ip, userAgent, username, loginDate);
		if(park!=null){
			throw new EntityAlreadyExistsException("already exists the attempt "+park);
		}
				
		this.failedLoginAttemptRepo.save(attempt);
		
	}

	public int deleteAllFailedLoginAttemptMadeBefore(Calendar date) {
		if(date==null) {
			return 0;
		}
		
		return this.failedLoginAttemptRepo.deleteAllFailedLoginAttemptBefore(date);
	}
	
	
}
