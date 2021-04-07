package it.palex.attendanceManagement.auth.service.auth;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.auth.dto.ConfirmPasswordDTO;
import it.palex.attendanceManagement.commons.messaging.email.CompileAndSendEmailService;
import it.palex.attendanceManagement.commons.security.CustomAuthenticationProvider;
import it.palex.attendanceManagement.commons.security.OneTimePasswordGeneratorService;
import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.entities.UserContacts;
import it.palex.attendanceManagement.data.entities.auth.ResetPasswordRequest;
import it.palex.attendanceManagement.data.entities.auth.ResetPasswordRequestPK;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.enumTypes.ContactTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedLangsEnumI18N;
import it.palex.attendanceManagement.data.service.user.ResetPasswordRequestService;
import it.palex.attendanceManagement.data.service.user.UserContactsService;
import it.palex.attendanceManagement.data.service.user.UserPasswordChangeHistoryService;
import it.palex.attendanceManagement.data.service.user.UsersAuthDetailsService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.HttpCodes;
import it.palex.attendanceManagement.library.utils.StringUtility;
import it.palex.attendanceManagement.library.utils.crypto.TokenGenerator;

@Service
public class ResetPasswordService implements GenericService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ResetPasswordService.class);
	
	@Value("${security.bruteforce.password_reset.reset_token_validity_seconds}")
	private int resetTokenValiditySeconds;
	
	@Autowired
	private CompileAndSendEmailService compileAndSendEmailSrv;
	
    @Autowired
    private ResetPasswordRequestService  resetPasswordRequestSrv;
    
    
    @Autowired
    private RequestPasswordChangeSecurityManager  requestPasswordChangeSecurityManager;
  
    @Autowired
	private UsersAuthDetailsService userAuthDetailsService;
    
	@Autowired
	private CustomAuthenticationProvider authenticationManager;
    
	@Autowired
	private OneTimePasswordGeneratorService oneTimePswGeneratorSrv;
	
	@Autowired
	private UserPasswordChangeHistoryService pswChangeHistorySrv;
	
	@Autowired
	private CompileAndSendEmailService compileAndSendEmailService;
	
	@Autowired
	private UserContactsService userContactSrv;
	
	
	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = HttpCodes.INTERNAL_SERVER_ERROR)
	public GenericResponse<StringDTO> resetChangePassword(String userAgent,
			HttpServletRequest httpRequest, String username) {
		if(userAgent==null || httpRequest==null || username==null){
			return this.buildBadDataResponse();
		}
		String requestToken = TokenGenerator.generateSecureTokenOf64Characters();		
		
		String succMessage = "If the username belongs to an user the reset instruction will be sent to it";
		
		UsersAuthDetails user = this.userAuthDetailsService.findByUsername(username);
		
		return completeResetPasswordRequest(userAgent, httpRequest, requestToken, succMessage, user);
					
	}

	private GenericResponse<StringDTO> completeResetPasswordRequest(String userAgent, 
			HttpServletRequest httpRequest, String requestToken,
			String succMessage, UsersAuthDetails user) {
		Date creationDate = DateUtility.getCurrentDateInUTC();
		Date expirationDate = DateUtility.addSecondsToCalendar(creationDate, this.resetTokenValiditySeconds);
		
		if(user==null){
			if(!this.requestPasswordChangeSecurityManager.canMakePasswordRecovery(requestToken, null, httpRequest, creationDate)){
				return this.buildTooManyRequest("Too many reset request received");
			}
			return this.buildOkResponse(new StringDTO(succMessage)); // do not tell to attacker that the user not exists or a bruteforce hole exists
		}
		
		String usernameAuth = user.getUsername();
		
		if(!this.requestPasswordChangeSecurityManager.canMakePasswordRecovery(requestToken, usernameAuth, 
				httpRequest, creationDate)){
			LOGGER.warn("ATTENTION TOO MANY REQUEST FOR RECOVERY ARE RECEIVED");
			return this.buildTooManyRequest("Too many reset request received");
		}
		
		return this.executePasswordResetRequest(succMessage, usernameAuth, httpRequest, 
				userAgent, creationDate, requestToken, expirationDate);
	}
	
	/**
	 * @param hashedUsername
	 * @param httpRequest
	 * @param userAgent
	 * @param expirationDate 
	 * @param email
	 * @return
	 */
	GenericResponse<StringDTO> executePasswordResetRequest(
			String succMessage, String username, HttpServletRequest httpRequest, 
			String userAgent, Date creationDate, String requestToken, Date expirationDate) {
		try{
			//TODO get lang of user
			this.compileAndSendEmailSrv.sendResetPswEmail(username, SupportedLangsEnumI18N.DEFAULT_USER_LANG(), 
					requestToken);
		
		}catch(SendFailedException | AddressException e){
			return this.buildInternalServerError("Recipient not found");
		}catch(MessagingException e){
			LOGGER.error("Error sending email ----- resetPassword", e);
			return this.buildInternalServerError("Error sending email");
		}catch(Exception e){
			return this.buildInternalServerError(e.getMessage());
		}
		
		String ip = this.requestPasswordChangeSecurityManager.getIpFromRequest(httpRequest);
		ResetPasswordRequest req = this.buildResetPasswordRequest(username, ip, 
				creationDate, requestToken, userAgent, expirationDate);
		
		this.resetPasswordRequestSrv.addResetPasswordRequest(req);
		
		return this.buildStringOkResponse(succMessage);	
	}
	
	private ResetPasswordRequest buildResetPasswordRequest(String username, String ip, 
			Date creationDate, String requestToken, String userAgent, Date expirationDate) {

		ResetPasswordRequest request = new ResetPasswordRequest();

		ResetPasswordRequestPK pk = new ResetPasswordRequestPK();

		pk.setCreationDate(creationDate);
		pk.setUsername(username);
		pk.setUserAgent(userAgent);
		pk.setIp(ip);

		request.setResetPasswordRequestPK(pk);
		request.setRequestToken(requestToken);
		request.setResetEmailSentSuccessfully(true);
		request.setExpirationDate(expirationDate);
		
		return request;

	}

	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.NOT_ACCEPTABLE, HttpCodes.INTERNAL_SERVER_ERROR})
	public GenericResponse<StringDTO> confirmPassword(ConfirmPasswordDTO cp) {
		if (cp == null || StringUtility.isEmptyOrWhitespace(cp.getToken()) || StringUtility.isEmptyOrWhitespace(cp.getPassword())) {
			return this.buildUnauthorizedResponse();
		}
		
		ResetPasswordRequest req=  this.resetPasswordRequestSrv.getByToken(cp.getToken());

		if(req==null) {
			return this.buildUnauthorizedResponse();
		}		
		Date date = req.getExpirationDate();
		
		if(date==null || DateUtility.getCurrentDateInUTC().after(date)) {
			return this.buildUnauthorizedResponse("Token is expired");
		}
		
				
		UsersAuthDetails user = this.userAuthDetailsService.findByUsername(req.getResetPasswordRequestPK().getUsername());
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		if(user.getHashedPassword()!=null) {
			boolean isThePasswordCurrentlyUsed = this.authenticationManager.checkBCryptPassword(cp.getPassword(), user.getHashedPassword());
			
			if(isThePasswordCurrentlyUsed) {
				return this.buildNotAcceptableResponse(StandardReturnCodesEnum.THIS_IS_YOUR_PASSWORD);
			}
			
		}
		
		
		user.setLastPasswordChangeDate(DateUtility.getCurrentDateInUTC());
		String hashedNewPassword = authenticationManager.hashPassword(cp.getPassword());
		user.setHashedPassword(hashedNewPassword);
		
		if(this.pswChangeHistorySrv.isPasswordRecentlyUsedByUser(hashedNewPassword, user.getId())){
			return this.buildNotAcceptableResponse(StandardReturnCodesEnum.PASSWORD_USED_RECENTLY);
		}
		
		this.pswChangeHistorySrv.addNewPswChange(cp.getPassword(), user, DateUtility.getCurrentDateInUTC());
		
		this.userAuthDetailsService.update(user);
		
		try {			
			UserContacts userContacts=this.userContactSrv.findByKey(user.getId(), ContactTypeEnum.EMAIL_ADDRESS);
			if(userContacts!=null) {		
			  this.compileAndSendEmailService.sendPasswordChangeCommunicationEmail(userContacts.getCValue(), SupportedLangsEnumI18N.DEFAULT_USER_LANG());
			}
			else{
				LOGGER.error("Error sending email Reset Password changed: mail user not already");
			}
		} catch (Exception e) {	
			return this.buildInternalServerError("Error sending email Reset Password changed");
		}
		
		return this.buildStringOkResponse("Successfully confirmed new Password");
	}

	
}
