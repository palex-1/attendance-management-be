package it.palex.attendanceManagement.data.dto.transformers;

import java.util.Date;

import it.palex.attendanceManagement.data.dto.core.UserProfileContractInfoDTO;
import it.palex.attendanceManagement.data.entities.UserProfileContractInfo;

public class UserProfileContractInfoTransformer {

	public static UserProfileContractInfoDTO mapToDTO(UserProfileContractInfo contractInfo, Date hiringDate,
			boolean includeCostInfo) {
		if(contractInfo==null) {
			return null;
		}
		UserProfileContractInfoDTO res = new UserProfileContractInfoDTO();
		res.setId(contractInfo.getId());
		res.setWorkDayHours(contractInfo.getWorkDayHours());
		res.setLeaveHours(contractInfo.getResidualLeaveHours());
		res.setVacationDays(contractInfo.getResidualVacationDays());
	 
		if(contractInfo.getEmploymentOffice()!=null) {
			res.setEmploymentOffice(contractInfo.getEmploymentOffice().getOfficeName());
		}
		
		if(includeCostInfo) {
			res.setHourlyCost(contractInfo.getHourlyCost());
		}
		
		res.setLevel(UserLevelTransformer.mapToDTO(contractInfo.getLevel()));
		res.setHiringDate(hiringDate);
		
		return res;
	}
	
}
