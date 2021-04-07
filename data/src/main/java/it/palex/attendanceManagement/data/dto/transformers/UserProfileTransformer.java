package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.UserProfileDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileSmallDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileTinyDTO;
import it.palex.attendanceManagement.data.dto.impiegato.ImpiegatoSmallDTO;
import it.palex.attendanceManagement.data.entities.UserProfile;

public class UserProfileTransformer {
	
	
	public static UserProfileDTO mapToDTO(UserProfile userProfile) {
		if(userProfile==null) {
			return null;
		}
		UserProfileDTO res = new UserProfileDTO();
		res.setBirthDate(userProfile.getBirthDate());
		res.setId(userProfile.getId());
		res.setName(userProfile.getName());
		res.setSex(userProfile.getSex());
		res.setSurname(userProfile.getSurname());
		res.setEmail(userProfile.getEmail());
		res.setCf(userProfile.getFiscalCode());
		res.setEmploymentDate(userProfile.getDateOfEmployment());
		res.setPhoneNumber(userProfile.getPhoneNumber());
				
		if(userProfile.getUsersAuthDetails()!=null) {
			if(userProfile.getUsersAuthDetails().getPermissionGroup()!=null) {
				res.setPermissionGroupLabel(
							PermissionGroupLabelTransformer.mapToDTO(userProfile.getUsersAuthDetails().getPermissionGroup())
						);
			}
			res.setAccountLocked(!userProfile.getUsersAuthDetails().isAccountNonLocked());
			res.setAccountDisabled(!userProfile.getUsersAuthDetails().isEnabled());
		}

		if(userProfile.getCompany()!=null) {
			res.setCompany(CompanyTransformer.mapToDTO(userProfile.getCompany()));
		}
		
		return res;
	}
	
	
	public static UserProfileDTO mapToDTO(UserProfile userProfile, String userProfileImageDownloadToken) {
		UserProfileDTO res = mapToDTO(userProfile);
		if(res==null) {
			return null;
		}
		res.setUserProfileImageDownloadToken(userProfileImageDownloadToken);
		
		return res;
	}
	

	
	public static UserProfileSmallDTO mapToSmallDTO(UserProfile profile) {
		if(profile==null) {
			return null;
		}
		UserProfileSmallDTO res = new UserProfileSmallDTO();
		res.setSurname(profile.getSurname());
		res.setName(profile.getName());
		res.setId(profile.getId());
		res.setEmail(profile.getEmail());
		res.setPhoneNumber(profile.getPhoneNumber());
		res.setSex(profile.getSex());
		
		if(profile.getCompany()!=null) {
			res.setCompany(CompanyTransformer.mapToDTO(profile.getCompany()));
		}
		
		return res;
	}
	
	public static UserProfileTinyDTO mapToTinyDTO(UserProfile profile) {
		if(profile==null) {
			return null;
		}
		UserProfileTinyDTO dto = new UserProfileTinyDTO();
		dto.setName(profile.getName());
		dto.setSurname(profile.getSurname());
		dto.setId(profile.getId());
		
		return dto;
	}


	public static List<UserProfileDTO> mapToDTO(List<UserProfile> list) {
		if(list==null) {
			return null;
		}
		
		
		List<UserProfileDTO> res = new ArrayList<>(list.size());
		
		for (UserProfile userProfile : list) {
			res.add(mapToDTO(userProfile));
		}
		
		return res;
	} 
	
}
