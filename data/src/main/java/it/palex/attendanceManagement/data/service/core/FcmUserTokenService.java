package it.palex.attendanceManagement.data.service.core;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.dto.core.AddFcmUserTokenDTO;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.core.FcmUserToken;
import it.palex.attendanceManagement.data.entities.core.QFcmUserToken;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedProvidersEnum;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.FcmUserTokenRepository;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.IterableUtils;
import it.palex.attendanceManagement.library.utils.StringUtility;

@Service
public class FcmUserTokenService implements GenericService {

	private final QFcmUserToken QFUT = QFcmUserToken.fcmUserToken;
	
	@Autowired
	private FcmUserTokenRepository fcmUserTokenRepo;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	/**
	 * 
	 * @param token
	 * @return the created token
	 * @throws NullPointerException if token is null
	 * @throws DataCannotBeInsertedInDatabase if token cannot be added in database
	 */
	public FcmUserToken save(FcmUserToken token) {
		if(token==null) {
			throw new NullPointerException();
		}
		
		if(!token.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(token);
		}
		
		return this.fcmUserTokenRepo.save(token);
	}
	

	public boolean deleteByTokenAndProvider(String token, String provider) {
		FcmUserToken ut = this.findByTokenAndProviderName(token, provider);
		
		if(ut==null) {
			return false;
		}
		this.fcmUserTokenRepo.delete(ut);
		
		return true;
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> saveOrUpdateToken(AddFcmUserTokenDTO fcmUserToken, 
				SupportedProvidersEnum provider){
		if(fcmUserToken==null || StringUtility.isEmptyOrWhitespace(fcmUserToken.getDeviceId())
				|| StringUtility.isEmptyOrWhitespace(fcmUserToken.getToken())) {
			return this.buildBadDataResponse();
		}
		
		UsersAuthDetails currentUser = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserAuthDetails();
		
		if(currentUser==null) {
			return this.buildUnauthorizedResponse("User is not authorized");
		}
		
		FcmUserToken oldToken = this.findByDeviceId(fcmUserToken.getDeviceId());
		
		if(oldToken!=null) {
			return this.updateOldToken(oldToken, provider, currentUser, fcmUserToken.getToken());
		}
		
		oldToken = this.findByTokenAndProviderName(fcmUserToken.getToken(), fcmUserToken.getProvider());
		
		if(oldToken!=null) {
			oldToken.setUserId(currentUser);
			this.fcmUserTokenRepo.save(oldToken);
			
			return this.buildStringOkResponse("Token updated successfully");
		}
		
		//crete a new token
		FcmUserToken newToken = new FcmUserToken();
		newToken.setDeviceId(fcmUserToken.getDeviceId());
		newToken.setProviderName(provider.name());
		newToken.setToken(fcmUserToken.getToken());
		newToken.setUserId(currentUser);
		
		if(!newToken.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse("Token info seems to be invalid");
		}
		
		this.fcmUserTokenRepo.save(newToken);
		
		return this.buildStringOkResponse("Token registered successfully");
	}
	
	public GenericResponse<StringDTO> saveOrUpdateToken(AddFcmUserTokenDTO notificationToken) {
		if(notificationToken==null
				|| StringUtility.isEmptyOrWhitespace(notificationToken.getProvider())) {
			return this.buildBadDataResponse();
		}

		if(!SupportedProvidersEnum.isValid(notificationToken.getProvider())) {
			return this.buildBadDataResponse();
		}
		
		return saveOrUpdateToken(notificationToken, 
				SupportedProvidersEnum.valueOf(notificationToken.getProvider()));
	}
	
	
	private GenericResponse<StringDTO> updateOldToken(FcmUserToken oldToken, 
			SupportedProvidersEnum provider, UsersAuthDetails currentUser, String token){
				
		oldToken.setProviderName(provider.name());
		oldToken.setToken(token);
		oldToken.setUserId(currentUser); //update user that own device
		
		if(!oldToken.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse("Token info seems to be invalid");
		}
		
		this.fcmUserTokenRepo.save(oldToken);
		
		return this.buildStringOkResponse("Token updated successfully");
	}
	
	
	public GenericResponse<StringDTO> revoke(String token, SupportedProvidersEnum provider) {
		if(StringUtility.isEmptyOrWhitespace(token) || provider==null) {
			return this.buildBadDataResponse();
		}
		
		return this.revoke(token, provider.name());
	}
	
	public GenericResponse<StringDTO> revoke(String token, String provider){
		if(StringUtility.isEmptyOrWhitespace(token) || 
				StringUtility.isEmptyOrWhitespace(provider)) {
			return this.buildBadDataResponse();
		}
		
		UsersAuthDetails currentUser = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserAuthDetails();
		
		if(currentUser==null) {
			return this.buildUnauthorizedResponse();
		}
		
		FcmUserToken toRevoke = this.findByTokenAndProviderName(token, provider);
		
		if(toRevoke==null) {
			return this.buildNotFoundResponse();
		}
		
		if(!toRevoke.getUserId().getId().equals(currentUser.getId())) {
			return this.buildUnauthorizedResponse(StandardReturnCodesEnum.DEVICE_ID_OWNED_BY_ONOTHER_USER);
		}
		
		this.fcmUserTokenRepo.delete(toRevoke);
		
		return this.buildStringOkResponse("Token successfully revoked");
	}
	
	
	/**
	 * @return a List of FcmUserToken 
	 */
	public List<FcmUserToken> findAllTokenOfUser(Integer userId){
		if(userId==null) {
			return new LinkedList<FcmUserToken>();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QFUT.userId.id.eq(userId));
		
		return IterableUtils.iterableToList(this.fcmUserTokenRepo.findAll(cond));
	}
	
	
	public FcmUserToken findByDeviceId(String deviceId) {
		if(deviceId==null) {
			return null;
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QFUT.deviceId.eq(deviceId));
		
		return this.getFirstResultFromIterable(this.fcmUserTokenRepo.findAll(cond));
	}
	
	public FcmUserToken findByTokenAndProviderName(String token, SupportedProvidersEnum provider) {
		if(provider==null) {
			return null;
		}
		return this.findByTokenAndProviderName(token, provider.name());
	}
	
	public FcmUserToken findByTokenAndProviderName(String token, String providerName) {
		if(token==null || providerName==null) {
			return null;
		}
		
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QFUT.token.eq(token));
		cond.and(QFUT.providerName.equalsIgnoreCase(providerName));
		
		return this.getFirstResultFromIterable(this.fcmUserTokenRepo.findAll(cond));
	}
	
	public FcmUserToken findByUserIdTokenAndProviderName(Integer userId, 
			String token, SupportedProvidersEnum provider) {
		if(provider==null) {
			return null;
		}
		
		return this.findByUserIdTokenAndProviderName(userId, token, provider.name());
	}


	public FcmUserToken findByUserIdTokenAndProviderName(Integer userId, String token, 
			String providerName) {
		if(userId==null || token==null || providerName==null) { 
			//not null constraint so nothing will be found
			return null;
		}
		
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QFUT.userId.id.eq(userId));
		cond.and(QFUT.token.eq(token));
		cond.and(QFUT.providerName.equalsIgnoreCase(providerName));
		
		return this.getFirstResultFromIterable(this.fcmUserTokenRepo.findAll(cond));
	}

	

	

}

