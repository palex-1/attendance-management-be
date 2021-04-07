package it.palex.attendanceManagement.auth.service.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.auth.dto.ChangePasswordDTO;
import it.palex.attendanceManagement.commons.messaging.email.CompileAndSendEmailService;
import it.palex.attendanceManagement.commons.security.CustomAuthenticationProvider;
import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.entities.UserContacts;
import it.palex.attendanceManagement.data.entities.auth.UsersAccessToken;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.enumTypes.ContactTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedLangsEnumI18N;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
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

@Service
public class ChangePasswordService implements GenericService {
	
	 private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ChangePasswordService.class);
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserSrv;
	
	@Autowired
	private CustomAuthenticationProvider authenticationManager;
	
	@Autowired
	private UsersAuthDetailsService userAuthDetailsService;
	
	@Autowired
	private UserPasswordChangeHistoryService pswChangeHistorySrv;
	
	@Autowired
	private CompileAndSendEmailService compileAndSendEmailService;
	
	@Autowired
	private UserContactsService userContactSrv;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	
	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.NOT_ACCEPTABLE, HttpCodes.INTERNAL_SERVER_ERROR})
	public GenericResponse<StringDTO> changePassword(ChangePasswordDTO changePsw) {
		if(changePsw==null) {
			return this.buildBadDataResponse();
		}
		String oldPassword = changePsw.getOldPassword();
		String newPassword = changePsw.getNewPassword();
		
		if (StringUtility.isEmptyOrWhitespace(oldPassword) 
				|| StringUtility.isEmptyOrWhitespace(newPassword)) {
			return this.buildForbiddenResponse();
		}

	    UsersAuthDetails user = this.currentAuthenticatedUserSrv.getCurrentAuthenticatedUserAuthDetails();
	    
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
	    if(!this.authenticationManager.checkBCryptPassword(oldPassword, user.getHashedPassword())) {
	    	return this.buildForbiddenResponse("Old password is wrong");
	    }
			
	    return completePasswordChange(newPassword, user);				
	}


	private GenericResponse<StringDTO> completePasswordChange(String newPassword, UsersAuthDetails user) {
		if(user.getHashedPassword()!=null) {
			boolean isThePasswordCurrentlyUsed = this.authenticationManager.checkBCryptPassword(newPassword, user.getHashedPassword());
			
			if(isThePasswordCurrentlyUsed) {
				return this.buildNotAcceptableResponse(StandardReturnCodesEnum.THIS_IS_YOUR_PASSWORD);
			}
			
		}
	    
		if(this.pswChangeHistorySrv.isPasswordRecentlyUsedByUser(newPassword, user.getId())){
			return this.buildNotAcceptableResponse(StandardReturnCodesEnum.PASSWORD_USED_RECENTLY);
		}
		Date currentDate = DateUtility.getCurrentDateInUTC();
		
		if(this.pswChangeHistorySrv.existsByKey(newPassword, user.getId(), currentDate)){
			return this.buildTooManyRequest("You have modified the password a second ago");
		}
			
		user.setLastPasswordChangeDate(currentDate);
		String hashedNewPassword = authenticationManager.hashPassword(newPassword);
		user.setHashedPassword(hashedNewPassword);
		
		user = this.userAuthDetailsService.saveOrUpdate(user);
		
		this.pswChangeHistorySrv.addNewPswChange(newPassword, user, currentDate);
			
		try {			
			UserContacts userContacts=this.userContactSrv.findByKey(user.getId(), ContactTypeEnum.EMAIL_ADDRESS);
			
			if(userContacts!=null) {		
			  this.compileAndSendEmailService.sendPasswordChangeCommunicationEmail(userContacts.getCValue(), SupportedLangsEnumI18N.DEFAULT_USER_LANG());
			}
		} catch (Exception e) {	
			LOGGER.error("Error sending email Password changed: mail user not already", e);
		}

		
		UsersAccessToken token = this.authenticationService.createAuthenticationTokenForUser(user);
		
		return this.buildStringOkResponse(token.getToken());
	}


	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST,
			HttpCodes.INTERNAL_SERVER_ERROR, HttpCodes.NOT_ACCEPTABLE})
	public GenericResponse<StringDTO> changePswForLogin(ChangePasswordDTO changePsw) {
		if(changePsw==null) {
			return this.buildBadDataResponse();
		}
		String newPassword = changePsw.getNewPassword();
		
		if (StringUtility.isEmptyOrWhitespace(newPassword)) {
			return this.buildBadDataResponse();
		}

	    UsersAuthDetails user = this.currentAuthenticatedUserSrv.getCurrentAuthenticatedUserAuthDetails();
	    
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		user.setMustResetPassword(false);
		
	    return completePasswordChange(newPassword, user);
	}
}
