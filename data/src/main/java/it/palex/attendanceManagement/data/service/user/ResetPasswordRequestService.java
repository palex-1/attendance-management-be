package it.palex.attendanceManagement.data.service.user;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.data.entities.auth.ResetPasswordRequest;
import it.palex.attendanceManagement.data.entities.auth.ResetPasswordRequestPK;
import it.palex.attendanceManagement.data.exceptions.EntityAlreadyExistsException;
import it.palex.attendanceManagement.data.exceptions.EntityCreationException;
import it.palex.attendanceManagement.data.repository.auth.ResetPasswordRequestRepository;
import it.palex.attendanceManagement.library.service.GenericService;

/**
 * @author Alessandro Pagliaro
 *
 */
@Service
public class ResetPasswordRequestService implements GenericService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ResetPasswordRequestService.class);
	
	@Autowired
	private ResetPasswordRequestRepository resetPasswordRequestRepo;
	
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws EntityCreationException if request cannot be added in database for constraints
	 * @throws EntityAlreadyExistsException if another request with same key already exists
	 */
	@Transactional
	public ResetPasswordRequest addResetPasswordRequest(ResetPasswordRequest request) {
		if(request==null){
			throw new NullPointerException();
		}
		if(!request.canBeInsertedInDatabase()){
			throw new EntityCreationException();
		}
		if(alreadyExists(request)){
			throw new EntityAlreadyExistsException("entity already exists");
		}
		
		//create entity
		this.resetPasswordRequestRepo.save(request);
		//return created entity
		
		return request;
	}	
	
	
	public boolean alreadyExists(ResetPasswordRequest request){
		if(request==null || request.getResetPasswordRequestPK()==null){
			throw new NullPointerException();
		}
		ResetPasswordRequestPK pk = request.getResetPasswordRequestPK();
		ResetPasswordRequest testKey = this.getByKey(pk.getIp(), pk.getUserAgent(), 
				pk.getCreationDate(), request.getResetPasswordRequestPK().getUsername());
		if(testKey!=null){
		   return true;
		}
		ResetPasswordRequest testUnique = this.getByToken(request.getRequestToken());
		if(testUnique!=null){
			return true;
		}
		return false;
		
	}
	
	public ResetPasswordRequest getByKey(String ip, String userAgent, Date creationDate, String username){
		if(ip==null || userAgent==null || creationDate==null || username==null){
			throw new NullPointerException("ip:"+ip+" userAgent+"+userAgent+" creationDate:"+creationDate+" username:"+username);
		}
		return this.resetPasswordRequestRepo.getByKey(ip, userAgent, creationDate, username);
	}
	
	public ResetPasswordRequest getByToken(String requestToken){
		if(requestToken==null){
			throw new NullPointerException("requestToken:"+requestToken);
		}
		
		return this.resetPasswordRequestRepo.getByToken(requestToken);
	}
	
	
	public ResetPasswordRequest addPasswordResetRequestAndInvalidateAllPreviouslyDone(ResetPasswordRequest request) throws EntityCreationException, EntityAlreadyExistsException {
		if(request==null){
			throw new NullPointerException();
		}
		
		ResetPasswordRequest req = this.addResetPasswordRequest(request);
		//after add
		
		Date beforeDate = req.getResetPasswordRequestPK().getCreationDate();
		Calendar before = Calendar.getInstance();
		before.setTime(beforeDate);
		
		this.deleteAllPasswordResetRequestBeforeDateOfUsername(req.getResetPasswordRequestPK().getUsername(), before);
		
		return req;
	}
	
	public int deleteAllPasswordResetRequestBeforeDateOfUsername(String username, Calendar beforeDate) {
		if(username==null || beforeDate==null){
			throw new NullPointerException("username:"+username+" beforeDate:"+beforeDate);
		}
		return this.resetPasswordRequestRepo.deleteAllPasswordResetRequestBeforeDateOfUsername(username, beforeDate.getTime());
	}
	
	
	public boolean deleteResetPasswordRequest(ResetPasswordRequest request) {
		if(request==null){
			throw new NullPointerException("request is null at deleteResetPasswordRequest");
		}
		
		if(!alreadyExists(request)){
			return false;
		}
		
		this.resetPasswordRequestRepo.delete(request);
		
		return true;
	}

	
	public long countResetPasswordRequestOfIpInTimeRange(String ip, Calendar from, Calendar to) {
		if(ip==null || from==null || to==null){
			throw new NullPointerException("ip:"+ip+" from:"+from+" to:"+to);
		}
		if(from.compareTo(to)>0){ 
			throw new IllegalArgumentException("From after to!! from:"+from+" to:"+to);
		}
		return this.resetPasswordRequestRepo.countResetPasswordRequestOfIpInTimeRange(ip, from.getTime(), to.getTime());
	}
	

	public long countResetPasswordRequestOfUsernameInTimeRange(String username, Calendar from,
			Calendar to) {
		 if(username==null || from==null || to==null){
				throw new NullPointerException("username:"+username+" from:"+from+" to:"+to);
		 }
		 if(from.compareTo(to)>0){ 
				throw new IllegalArgumentException("From after to!! from:"+from+" to:"+to);
		 }
		 return this.resetPasswordRequestRepo.countResetPasswordRequestOfUsernameInTimeRange(username, from.getTime(), to.getTime());
	}
	
	public long countResetPasswordRequestOfUsernameInTimeRange(String username, Date from,
			Date to) {
		 if(username==null || from==null || to==null){
				throw new NullPointerException("username:"+username+" from:"+from+" to:"+to);
		 }
		 if(from.compareTo(to)>0){ 
				throw new IllegalArgumentException("From after to!! from:"+from+" to:"+to);
		 }
		 return this.resetPasswordRequestRepo.countResetPasswordRequestOfUsernameInTimeRange(username, from, to);
	}
	
	public boolean existRecoveryAttemptForUserInDate(String username, Calendar currentDate) {
		if(username==null || currentDate==null){
			throw new NullPointerException("username:"+username+" currentDate:"+currentDate);
		}
		
		return this.resetPasswordRequestRepo.existRecoveryAttemptForUserInDate(username, currentDate.getTime());
	}

	public boolean existRecoveryAttemptForUserInDate(String username, Date currentDate) {
		if(username==null || currentDate==null){
			throw new NullPointerException("username:"+username+" currentDate:"+currentDate);
		}
		
		return this.resetPasswordRequestRepo.existRecoveryAttemptForUserInDate(username, currentDate);
	}
	
	public int deleteAllResetPasswordExpiredBefore(Date before) {
		if(before==null){
			throw new NullPointerException("null before at deleteAllResetPasswordRequestBefore");
		}
		
		return this.resetPasswordRequestRepo.deleteAllResetPasswordExpiredBefore(before);
	}
	
	
	public List<ResetPasswordRequest> getAllResetPasswordRequestOfUsername(String username) {
		if(username==null){
			throw new NullPointerException();
		}
		return this.resetPasswordRequestRepo.getAllResetPasswordRequestOfUsername(username);
	}

	/**
	 * 
	 * @param token
	 * @throws EntityNotExistsException if no request exist with specified token
	 */
	public void invalidatePasswordResetRequestToken(String token){
		if(token==null){
			throw new NullPointerException("Null token at invalidateAllPasswordResetRequestToken method");
		}
		
		ResetPasswordRequest request = this.resetPasswordRequestRepo.getByToken(token);
		
		if(request==null){
			throw new EntityNotFoundException();
		}
		
		this.resetPasswordRequestRepo.delete(request);
	}
	
}
