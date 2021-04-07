package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.UserLevelDTO;
import it.palex.attendanceManagement.data.entities.core.UserLevel;

public class UserLevelTransformer {

	public static UserLevelDTO mapToDTO(UserLevel level) {
		if(level==null) {
			return null;
		}
		UserLevelDTO park = new UserLevelDTO();
		park.setId(level.getId());
		park.setLevel(level.getLevel());
		park.setMonthlyVacationDays(level.getMonthlyVacationDays());
		park.setMonthlyLeaveHours(level.getMonthlyLeaveHours());
		park.setBankHourEnabled(level.getBankHourEnabled());
		park.setExtraWorkPaid(level.getExtraWorkPaid());
		
		
		return park;
	}
	
	public static List<UserLevelDTO> mapToDTO(List<UserLevel> levels) {
		if(levels==null) {
			return null;
		}
		
		List<UserLevelDTO> res = new ArrayList<UserLevelDTO>();
		
		for (UserLevel level : levels) {
			res.add(mapToDTO(level));
		}
		
		return res;
	}

}
