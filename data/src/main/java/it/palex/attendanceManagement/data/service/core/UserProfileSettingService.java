package it.palex.attendanceManagement.data.service.core;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.dto.core.UserProfileSettingsDTO;
import it.palex.attendanceManagement.data.dto.transformers.UserProfileSettingsTransformer;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.QUserProfileSetting;
import it.palex.attendanceManagement.data.entities.core.UserProfileSetting;
import it.palex.attendanceManagement.data.entities.enumTypes.UserProfileSettingTuple;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.UserProfileSettingRepository;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.IterableUtils;

@Service
public class UserProfileSettingService implements GenericService {

	private final QUserProfileSetting QUPS = QUserProfileSetting.userProfileSetting;
	
	@Autowired
	private UserProfileSettingRepository userProfileSettingRepository;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	/**
	 * 
	 * @param setting
	 * @return the saved or updated user profile setting
	 */
	public UserProfileSetting saveOrUpdate(UserProfileSetting setting) {
		if(setting==null) {
			throw new NullPointerException();
		}
		if(!setting.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(setting);
		}
		
		UserProfileSetting park = findByUnique(setting.getUserProfile(), setting.getSettingArea(), setting.getSettingKey());
		
		if(park!=null) { //already exists
			park.setSettingValue(setting.getSettingValue());
			
			return this.userProfileSettingRepository.save(park);
		}
				
		return this.userProfileSettingRepository.save(setting);
	}
	
	/**
	 * 
	 * @param profile
	 * @param areaName
	 * @param keyName
	 * @return null if one of input params is null otherwise the profile setting if exists otherwise null
	 */
	public UserProfileSetting findByUnique(UserProfile profile, String areaName, String keyName) {
		if(profile==null || areaName==null || keyName==null) {
			return null;
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPS.userProfile.id.eq(profile.getId()));
		cond.and(QUPS.settingArea.eq(areaName));
		cond.and(QUPS.settingKey.eq(keyName));
		
		UserProfileSetting setting = this.getFirstResultFromIterable(
					this.userProfileSettingRepository.findAll(cond)
				);
		
		return setting;
	}
	
	
	public static boolean checkIfSettingValueIsTrue(UserProfileSetting setting) {
		if(setting==null) {
			return false;
		}
		return StringUtils.equalsIgnoreCase(setting.getSettingValue(), UserProfileSetting.TRUE_PROPERTY_VALUE);
	}

	public GenericResponse<List<UserProfileSettingsDTO>> findAllMySettings() {
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		List<UserProfileSetting> settings = this.findAllUserSetting(profile.getId()); 
		
		List<UserProfileSettingsDTO> res = UserProfileSettingsTransformer.mapToDTO(settings);
		
		return this.buildOkResponse(res);
	}
	
	
	public List<UserProfileSetting> findAllUserSetting(Integer userProfileId){
		if(userProfileId==null) {
			return new LinkedList<UserProfileSetting>();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPS.userProfile.id.eq(userProfileId));
		
		List<UserProfileSetting> list = IterableUtils.iterableToList(this.userProfileSettingRepository.findAll(cond));
	
		return list;
	}

	public boolean checkIfPushNotificationAreEnabled(UserProfile profile) {
		UserProfileSetting setting = this.findByUnique(profile, 
				UserProfileSettingTuple.PUSH_NOTIFICATION.AREA_NAME,
				UserProfileSettingTuple.PUSH_NOTIFICATION.ENABLED);
		
		if(setting==null) {
			return false;
		}
		
		return UserProfileSettingService.checkIfSettingValueIsTrue(setting);
	}

	
	
}
