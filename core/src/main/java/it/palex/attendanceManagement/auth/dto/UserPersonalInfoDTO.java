package it.palex.attendanceManagement.auth.dto;

import it.palex.attendanceManagement.data.dto.core.UserProfileAddressDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileContractInfoDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileDTO;

public class UserPersonalInfoDTO {

	private UserProfileDTO userProfile;
	private UserProfileAddressDTO domicile;
	private UserProfileAddressDTO residence;
	private UserProfileContractInfoDTO contractInfo;
	
	public UserProfileDTO getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfileDTO userProfile) {
		this.userProfile = userProfile;
	}
	
	public UserProfileAddressDTO getDomicile() {
		return domicile;
	}
	public void setDomicile(UserProfileAddressDTO domicile) {
		this.domicile = domicile;
	}
	
	public UserProfileAddressDTO getResidence() {
		return residence;
	}
	public void setResidence(UserProfileAddressDTO residence) {
		this.residence = residence;
	}
	public UserProfileContractInfoDTO getContractInfo() {
		return contractInfo;
	}
	public void setContractInfo(UserProfileContractInfoDTO contractInfo) {
		this.contractInfo = contractInfo;
	}
	
	
	
}
