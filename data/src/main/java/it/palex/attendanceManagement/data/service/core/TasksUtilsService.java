package it.palex.attendanceManagement.data.service.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.data.dto.tasks.SpecialTasksConfigDTO;
import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.utils.GlobalConfigurationUtils;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;

@Service
public class TasksUtilsService implements GenericService {

	@Autowired
	private GlobalConfigurationsService globalConfigurationsService;
	
	
	public GenericResponse<List<SpecialTasksConfigDTO>> findSpecialTasksConfig(){
		List<SpecialTasksConfigDTO> res = findSpecialTasksConfigs();
		
		return this.buildOkResponse(res);
	}
	
	
	
	public List<SpecialTasksConfigDTO> findSpecialTasksConfigs(){
		List<GlobalConfigurations> taskCodesConfig = this.globalConfigurationsService
				.findAllByArea(GlobalConfigurationSettingsTuple.CODICI_INCARICO_SPECIALI.AREA_NAME);
		
		List<SpecialTasksConfigDTO> res = new ArrayList<SpecialTasksConfigDTO>();
		
		List<GlobalConfigurations> taskColorsConfig = this.globalConfigurationsService
				.findAllByArea(GlobalConfigurationSettingsTuple.CODICI_INCARICO_SPECIALI_COLOR.AREA_NAME);
		
		for (GlobalConfigurations taskConfig : taskCodesConfig) {
			//in the value is saved the real  code of the task
			String taskCode = taskConfig.getSettingValue();
			
			//is the task key the same used for color
			String key = taskConfig.getSettingKey();
			
			//find the color with the same key but in the area of codici labels
			String hexColor = GlobalConfigurationUtils.getValueSkippingArea(taskColorsConfig, key);
			
			SpecialTasksConfigDTO park = new SpecialTasksConfigDTO();
			park.setName(key);
			park.setTaskCode(taskCode);
			park.setHexColor(hexColor);
			
			res.add(park);
		}
		
		return res;
	}
	
	
	

	
	
	
	
	
}
