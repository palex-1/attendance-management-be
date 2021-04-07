package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.UserProfileSettingsDTO;
import it.palex.attendanceManagement.data.entities.core.UserProfileSetting;

public class UserProfileSettingsTransformer {

	public static List<UserProfileSettingsDTO> mapToDTO(List<UserProfileSetting> settings){
		if(settings==null) {
			return null;
		}
		List<UserProfileSettingsDTO> res = new ArrayList<>();
		
		for(UserProfileSetting setting: settings) {
			res.add(mapToDTO(setting));
		}
		
		return res;
	}
	
	
	public static UserProfileSettingsDTO mapToDTO(UserProfileSetting setting) {
		if(setting==null) {
			return null;
		}
		UserProfileSettingsDTO res = new UserProfileSettingsDTO();
		res.setEditable(setting.getEditable());
		res.setSettingArea(setting.getSettingArea());
		res.setSettingKey(setting.getSettingKey());
		res.setSettingValue(setting.getSettingValue());
		res.setId(setting.getId());
		
		return res;
	}
	
}
