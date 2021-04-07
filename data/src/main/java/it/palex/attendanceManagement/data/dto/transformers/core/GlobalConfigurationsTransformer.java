package it.palex.attendanceManagement.data.dto.transformers.core;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.settings.GlobalConfigurationsDTO;
import it.palex.attendanceManagement.data.entities.GlobalConfigurations;

public class GlobalConfigurationsTransformer {

	public static GlobalConfigurationsDTO mapToDTO(GlobalConfigurations config) {
		if(config==null) {
			return null;
		}
		GlobalConfigurationsDTO res = new GlobalConfigurationsDTO();
		res.setEditable(config.getEditable());
		res.setVisible(config.getVisible());
		res.setId(config.getId());
		res.setSettingArea(config.getSettingArea());
		res.setSettingKey(config.getSettingKey());
		res.setSettingValue(config.getSettingValue());
		
		return res;
	}
	
	
	public static List<GlobalConfigurationsDTO> mapToDTO(List<GlobalConfigurations> configs){
		if(configs==null) {
			return null;
		}
		
		List<GlobalConfigurationsDTO> res = new ArrayList<GlobalConfigurationsDTO>(configs.size());
		
		for (GlobalConfigurations config : configs) {
			res.add(mapToDTO(config));
		}

		return res;
	}
	
	
	
	
	
	
	
}
