package it.palex.attendanceManagement.core.dtos.userProfile;

import java.util.Date;

import it.palex.attendanceManagement.data.dto.core.UserLevelDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileContractInfoDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileSmallDTO;

public class UserProfileHomeDTO {

	private Date lastUpdateDate;
	private UserProfileContractInfoDTO contractInfo;	
	private UserLevelDTO level;
	private UserProfileSmallDTO userProfile;

	
	
	public UserProfileContractInfoDTO getContractInfo() {
		return contractInfo;
	}

	public void setContractInfo(UserProfileContractInfoDTO contractInfo) {
		this.contractInfo = contractInfo;
	}

	public UserLevelDTO getLevel() {
		return level;
	}

	public void setLevel(UserLevelDTO level) {
		this.level = level;
	}

	public UserProfileSmallDTO getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfileSmallDTO userProfile) {
		this.userProfile = userProfile;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}
