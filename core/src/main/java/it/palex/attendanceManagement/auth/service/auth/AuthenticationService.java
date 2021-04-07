package it.palex.attendanceManagement.auth.service.auth;

import java.util.Collection; 
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.auth.dto.AuthenticationDTO;
import it.palex.attendanceManagement.auth.dto.AuthoritiesDTO;
import it.palex.attendanceManagement.auth.dto.CompleteAuthenticationDTO;
import it.palex.attendanceManagement.commons.messaging.email.CompileAndSendEmailService;
import it.palex.attendanceManagement.commons.messaging.sms.CompileAndSendSMSService;
import it.palex.attendanceManagement.commons.security.CustomAuthenticationProvider;
import it.palex.attendanceManagement.commons.security.JwtTokenUtil;
import it.palex.attendanceManagement.commons.security.OneTimePasswordGeneratorService;
import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.entities.UserContacts;
import it.palex.attendanceManagement.data.entities.auth.UsersAccessToken;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.enumTypes.ContactTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedLangsEnumI18N;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.auth.UserAccessTokenService;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.user.UserContactsService;
import it.palex.attendanceManagement.data.service.user.UsersAuthDetailsService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.exception.UnauthorizedException;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.HttpCodes;
import it.palex.attendanceManagement.library.utils.StringUtility;

@Component
public class AuthenticationService implements GenericService {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AuthenticationService.class);

	@Autowired
	private UserAccessTokenService userAccessTokenSrv;
	
	@Autowired
	private GlobalConfigurationsService globalConfigSrv;
	
	@Autowired
	private CustomAuthenticationProvider authenticationManager;
	
	@Autowired
	private LoginAttemptManager loginAttemptManager;
	
	@Autowired
	private UsersAuthDetailsService userAuthDetailsSrv;
	
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserContactsService userContactSrv;
	
	@Autowired
	private OneTimePasswordGeneratorService pswGeneratorSrv;
	
	@Autowired
	private CompileAndSendEmailService compileAndSendEmailSrv;
	
	@Autowired
	private CompileAndSendSMSService compileAndSendSMSSrv;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	@Value("${security.jwt.duration}")
	private int jwtDuration = 10;
	
	@Value("${security.hmac.otp.duration}")
	private int otpExpirationSeconds;
	
	
	public GenericResponse<AuthoritiesDTO> getUserAuthorityList(){
		GenericResponse<AuthoritiesDTO> response = new GenericResponse<>();
		List<String> authorities = new LinkedList<String>();
		response.setCode(HttpStatus.OK.value());
		response.setMessage("");
		if(SecurityContextHolder.getContext()!=null){
			if(SecurityContextHolder.getContext().getAuthentication()!=null){
				Collection<? extends GrantedAuthority> granthedAuth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
				if(granthedAuth!=null){
					granthedAuth.forEach( 
							park -> authorities.add(park.getAuthority())
						  );
				}
			}
		}
		AuthoritiesDTO park = new AuthoritiesDTO();
		park.setAuthorities(authorities);
		response.setData(park);
		
		return response;
	}

	public GenericResponse<StringDTO> completeAuthentication(CompleteAuthenticationDTO auth) {
		if(auth==null || auth.getOtp()==null || StringUtility.isEmptyOrWhitespace(auth.getToken())) {
			return this.buildBadDataResponse();
		}
				
		UsersAccessToken token = this.userAccessTokenSrv.findByToken( auth.getToken());
		
		if(token==null) {
			return this.buildUnauthorizedResponse("Invalid token");
		}
		
		Date currentTime = DateUtility.getCurrentDateInUTC();
		
		//TODO check that otp is not requested many times
		//return this.buildTooManyRequest()
		
		if(currentTime.after(token.getExpirationDate())) {
			return this.buildForbiddenResponse(StandardReturnCodesEnum.EXPIRED_OTP_CODE);
		}
		
		if(!token.isTwoFaInProgress()) {
			return this.buildBadDataResponse("2 fa is not in progress");
		}
		
		if(token.getOneTimePasswordReqNumber().intValue() > getMaxOTPFailedAttempt()) {
			return this.buildForbiddenResponse(StandardReturnCodesEnum.MAX_OTP_ATTEMPT_REACHED);
		}
		
		token.setOneTimePasswordReqNumber(token.getOneTimePasswordReqNumber() + 1);
		
		//protection against user that has discovered the password of user
		if(!StringUtils.equals(token.getOneTimePassword(), auth.getOtp())){
			this.userAccessTokenSrv.update(token);
			return this.buildForbiddenResponse(StandardReturnCodesEnum.INVALID_OTP_CODE);
		}
		
		token.setTwoFaInProgress(false);
		
		this.userAccessTokenSrv.update(token);
		
		if(BooleanUtils.isTrue(token.getMustResetPassword())) {
			return this.buildStringOkResponse("Token is now validated. You now must change password", StandardReturnCodesEnum.MUST_CHANGE_PASSWORD);
		}
		
		
		return this.buildStringOkResponse("Token is now validated");
	}

	private int getMaxOTPFailedAttempt() {
		return 5; //TODO save max otp failed attempt in configurations
	}
	
	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.INTERNAL_SERVER_ERROR})
	public GenericResponse<StringDTO> authenticate( AuthenticationDTO auth, String userAgent,
			HttpServletRequest httpRequest ) {
		
		if(auth==null || auth.getUsername()==null || auth.getPassword()==null){
			return this.buildUnauthorizedResponse("Bad credentials");
		}
		
		String username = auth.getUsername();
		
		try{			
			GenericResponse<StringDTO> res = this.loginAttemptManager.canMakeAuthentication(
					username, httpRequest);
					
			if(res.getCode()!=HttpCodes.OK){
				return res;
			}
			
			final Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                		auth.getUsername(), auth.getPassword()
	                )
	        );
			
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			final UsersAuthDetails userDetails = userAuthDetailsSrv.findByUsername(username);
			this.loginAttemptManager.saveSuccessfullLogin(username, httpRequest);
			
			UsersAccessToken accessToken = new UsersAccessToken();
			final Date expirationDate = calculateExpireDateFromNowTime();
	        final Date currentDate = DateUtility.getCurrentDateInUTC();
	        accessToken.setExpirationDate(expirationDate);
	        accessToken.setIssuedDate(currentDate);
	        accessToken.setFkIdUsersAuthDetails(userDetails);
	        
	        String token = this.jwtTokenUtil.generateToken(userDetails, expirationDate);
	        	
	        accessToken.setToken(token);
	        
	        if(isSigleDeviceLoginEnabled()) {
	        	this.killAllAuthenticationTokenOfUser(userDetails);
	        }
				
			if(userDetails.getTwoFaEnabled()) {
				return this.twoFactorAuthenticationFlowStart(accessToken);
			}else {
				return this.standardAuthenticationFlowStart(accessToken);
			}
			
		}catch(BadCredentialsException e){
    		this.loginAttemptManager.saveFailedLoginAttempt(username, httpRequest);
    		return this.buildUnauthorizedResponse("Bad credentials");
    	}catch(UnauthorizedException e) {
    		return this.buildUnauthorizedResponse(e.getMessage(), e.getCode());
    	}
	}
	
	
	public UsersAccessToken createAuthenticationTokenForUser(UsersAuthDetails userDetails) {
		if(userDetails==null) {
			throw new NullPointerException();
		}
		UsersAccessToken accessToken = new UsersAccessToken();
		final Date expirationDate = calculateExpireDateFromNowTime();
        final Date currentDate = DateUtility.getCurrentDateInUTC();
        accessToken.setExpirationDate(expirationDate);
        accessToken.setIssuedDate(currentDate);
        accessToken.setFkIdUsersAuthDetails(userDetails);
        accessToken.setTwoFaInProgress(false);
		accessToken.setOneTimePassword(null);
		accessToken.setMustResetPassword(false);
		
        String token = this.jwtTokenUtil.generateToken(userDetails, expirationDate);
        accessToken.setToken(token);
        
		return this.userAccessTokenSrv.create(accessToken);
	}
	
	private void killAllAuthenticationTokenOfUser(UsersAuthDetails user) {
		this.userAccessTokenSrv.deleteAllTokenOfUser(user);
	}
	
	private GenericResponse<StringDTO> twoFactorAuthenticationFlowStart(UsersAccessToken accessToken){
        Integer userAuthDetailsID = accessToken.getFkIdUsersAuthDetails().getId();
		UserContacts phoneNumberContact = this.userContactSrv.findByKey(userAuthDetailsID, ContactTypeEnum.PHONE_NUMBER);
		
		String phoneNumber = phoneNumberContact==null ? null:phoneNumberContact.getCValue();
				
		final String oneTimePsw = this.pswGeneratorSrv.generateHmacNumericOneTimePassword();

		SupportedLangsEnumI18N userLang = null;
		
		if(phoneNumber==null) {
			LOGGER.error("Cannot make Two Factor auth. Phone number is null for userID:"+userAuthDetailsID);
			return this.buildInternalServerError("Phone number is null for userID:"+userAuthDetailsID);
		}
		
		LOGGER.error("Cannot make Two Factor auth. Phone number is null for userID:"+userAuthDetailsID);
		boolean smsSent = sendSms(phoneNumber, oneTimePsw, userLang);
		
		if(!smsSent) {
			return this.buildInternalServerError("Cannot send no Sms OTP");
		}
		
		Date currentTime = DateUtility.getCurrentDateInUTC();
		Date expirationDate = DateUtility.addSecondsToDate(currentTime, otpExpirationSeconds);
		
		accessToken.setOneTimePassword(oneTimePsw);
		accessToken.setTwoFaInProgress(true);
		accessToken.setExpirationDate(expirationDate);
		
		if(BooleanUtils.isTrue(accessToken.getFkIdUsersAuthDetails().getMustResetPassword())) {
			accessToken.setMustResetPassword(true);
        }else {
        	accessToken.setMustResetPassword(false);
        }
		
		this.userAccessTokenSrv.create(accessToken);
		
		return this.buildStringOkResponse(accessToken.getToken(), StandardReturnCodesEnum.TWO_FA_REQUIRED_CODE);
	}
	
	private GenericResponse<StringDTO> standardAuthenticationFlowStart(UsersAccessToken accessToken){
		accessToken.setTwoFaInProgress(false);
		accessToken.setOneTimePassword(null);
		
		if(BooleanUtils.isTrue(accessToken.getFkIdUsersAuthDetails().getMustResetPassword())) {
			accessToken.setMustResetPassword(true);
			this.userAccessTokenSrv.create(accessToken);
			final StringDTO res = new StringDTO(accessToken.getToken());
			
			return this.buildOkResponse(res, StandardReturnCodesEnum.MUST_CHANGE_PASSWORD);
        }else {
        	accessToken.setMustResetPassword(false);
        	this.userAccessTokenSrv.create(accessToken);
    		final StringDTO res = new StringDTO(accessToken.getToken());
    		
    		return this.buildOkResponse(res);
        }
	}
	
	private boolean sendSms(String phoneNumber, String oneTimePsw, SupportedLangsEnumI18N userLang) {
		SupportedLangsEnumI18N lang = SupportedLangsEnumI18N.DEFAULT_USER_LANG();
		if(userLang!=null) {
			lang = userLang;
		}
		try{
			this.compileAndSendSMSSrv.sendOneTimePasswordSMS(phoneNumber, lang, oneTimePsw);
			return true;
		}catch(Exception e) {
			LOGGER.error("Error sending oneTimePsw SMS", e);
			return false;
		}
	}
	
	private boolean sendEmail(String email, String oneTimePsw, SupportedLangsEnumI18N userLang) {
		try {
			this.compileAndSendEmailSrv.sendOneTimePasswordEmail(email, userLang, oneTimePsw);
			return true;
		}catch(Exception e) {
			LOGGER.error("Error sending oneTimePsw Email", e);
			return false;
		}
	}
	
	private Date calculateExpireDateFromNowTime() {
		Date date = DateUtility.getCurrentDateInUTC();
		
		return DateUtility.addSecondsToCalendar(date, this.jwtDuration);
	}
	
	private boolean isSigleDeviceLoginEnabled() {
		String config = this.globalConfigSrv.getConfigValue(GlobalConfigurationSettingsTuple.LOGIN_SETTINGS.AREA_NAME, 
        				GlobalConfigurationSettingsTuple.LOGIN_SETTINGS.USE_ONLY_ONE_SESSION);
		if(config==null) {
			return false;
		}
		return Boolean.parseBoolean(config);
	}
}
