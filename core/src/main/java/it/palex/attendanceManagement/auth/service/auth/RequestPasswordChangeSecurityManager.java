package it.palex.attendanceManagement.auth.service.auth;

import java.util.Calendar; 
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.entities.auth.ResetPasswordRequest;
import it.palex.attendanceManagement.data.entities.auth.ResetPasswordRequestPK;
import it.palex.attendanceManagement.data.exceptions.EntityAlreadyExistsException;
import it.palex.attendanceManagement.data.exceptions.EntityCreationException;
import it.palex.attendanceManagement.data.service.user.ResetPasswordRequestService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.InformationHttpRequestExtractor;

/**
 * @author Alessandro Pagliaro
 *
 */
@Component
public class RequestPasswordChangeSecurityManager {

	@Value("${security.proxy.type:IGNORE}")
	private String proxyType;
	
	@Value("${security.change-password.days-to-forbit-reuse:60}")
	private int numberOfDaysToForbitPasswordReuse;
	
	@Value("${security.bruteforce.password_reset.max_failed_password_reset_attempt_single_ip}")
    private int maxFailedPasswordRecoveryAttemptForSingleIp;
    
    @Value("${security.bruteforce.password_reset.single_ip_password_reset_bruteforce_time_range}")
    private int secondsFailedPasswordRecoveryAttemptForSingleIP;
    
    @Value("${security.bruteforce.password_reset.max_failed_password_reset_attempt_single_account}")
    private int maxFailedPasswordRecoveryAttemptForSingleAccount;

    @Value("${security.bruteforce.password_reset.single_account_password_reset_bruteforce_time_range}")
    private int secondsFailedPasswordRecoveryAttemptForSingleAccount;
	
    @Autowired
	private ResetPasswordRequestService resetPswService;
    
    
	/**
	 * 
	 * @param hashedUsername
	 * @param httpRequest
	 * @return if the attempt is valid and save the made attempt if cannot be done
	 */
	public boolean canMakePasswordRecovery(String requestToken, String username, HttpServletRequest httpRequest, Date creationDate){
		if(httpRequest==null || creationDate==null){
			throw new NullPointerException("httpRequest is null");
		}
		return this.checkAttemptByID(username, httpRequest, creationDate, requestToken);	
	}
	
	
	private boolean checkAttemptByID(String username, HttpServletRequest httpRequest, Date creationDate, String requestToken){
		if(httpRequest==null){
			return false; //if the id is not found consider it as an attack
		}
		String ip = this.getIpFromRequest(httpRequest);
		String userAgent = InformationHttpRequestExtractor.getUserAgentFromRequest(httpRequest);
		if(ip==null || userAgent==null){
			return false;
		}
		
		boolean canBeMakedByIp = this.checkCanMakePasswordRecoveryByIP(ip);
	    
		if(!canBeMakedByIp){
			if(username==null){
				this.addInvalidResetPasswordRequest(this.buildInvalidResetPasswordRequest(requestToken, ResetPasswordRequestPK.DEFAULT_USERNAME_FOR_USERNAME_NOT_FOUND, ip, userAgent, creationDate));
			}else{
				this.addInvalidResetPasswordRequest(this.buildInvalidResetPasswordRequest(requestToken, username, ip, userAgent, creationDate));
			}
			return false;
		}
		if(username!=null){
			boolean canBeMakedByID = checkCanMakePasswordRecoveryByUsername(username,  httpRequest, creationDate);
			if(!canBeMakedByID){
				this.addInvalidResetPasswordRequest(this.buildInvalidResetPasswordRequest(requestToken, username, ip, userAgent, creationDate));
				return false;
			}
		}
		return true;
	}
	
	
	private boolean addInvalidResetPasswordRequest(ResetPasswordRequest request){
		try {
			this.resetPswService.addResetPasswordRequest(request);
			return true;
		}catch(EntityCreationException | EntityAlreadyExistsException e){
			return false;
		}
	}
	
	private ResetPasswordRequest buildInvalidResetPasswordRequest(String requestToken, String username, String ip, String userAgent, Date currentDate){
		ResetPasswordRequest req = new ResetPasswordRequest();
		req.setResetEmailSentSuccessfully(false);
		req.setRequestToken(requestToken);
		
		ResetPasswordRequestPK pk = new ResetPasswordRequestPK();
		pk.setCreationDate(currentDate);
		pk.setUsername(username);
		pk.setIp(ip);
		pk.setUserAgent(userAgent);
		
		req.setResetPasswordRequestPK(pk);
		
		return req;
	}
	
	/**
	 * @param email
	 * @param httpRequest
	 * @return
	 */
	private boolean checkCanMakePasswordRecoveryByUsername(String username, HttpServletRequest httpRequest, Date currentDate) {
		if(username==null){
			return false;
		}
		
		if(thereIsAnotherPasswordRecoveryAttemptMadeInSameSecond(username, currentDate)){
			return false;
		}
		
		Date from = DateUtility.addSecondsToDate(currentDate, this.secondsFailedPasswordRecoveryAttemptForSingleAccount * -1);
		
		long count = resetPswService.countResetPasswordRequestOfUsernameInTimeRange(username, from, currentDate);
		
		return count < this.maxFailedPasswordRecoveryAttemptForSingleAccount;
	}
	
	
	
	/**
	 * @param userAuthDetailsID
	 * @param currentDate
	 * @return
	 */
	private boolean thereIsAnotherPasswordRecoveryAttemptMadeInSameSecond(String hashedUsername, Date currentDate) {
		return resetPswService.existRecoveryAttemptForUserInDate(hashedUsername, currentDate);
	}

	
	/**
	 * @param email
	 * @param httpRequest
	 * @return false id the reset cannot be made, false will be returned if the ip is not preset in requets
	 */
	private boolean checkCanMakePasswordRecoveryByIP(String ip) {
		
		Calendar currentDate = DateUtility.getCurrentCalendarInUTC();
		
		Calendar from = DateUtility.addSecondsToCalendar(currentDate, this.secondsFailedPasswordRecoveryAttemptForSingleIP * - 1);

		long count = resetPswService.countResetPasswordRequestOfIpInTimeRange(ip, from, currentDate);
		
		return count < this.maxFailedPasswordRecoveryAttemptForSingleIp; // return true if can be maked
		
	}
	
	/**
	 * 
	 * @param httpRequest
	 * @return ip from request using ClientIPAddressRetriever, null will be returned if ip is not valid
	 */
	public String getIpFromRequest(HttpServletRequest httpRequest){
		if(this.proxyType==null){
			throw new NullPointerException("Parameter configSrv.getProxyType() is null");
		}
		return InformationHttpRequestExtractor.getIpFromRequest(httpRequest, this.proxyType);
	}


}