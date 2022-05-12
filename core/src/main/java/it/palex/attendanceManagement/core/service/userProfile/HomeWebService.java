package it.palex.attendanceManagement.core.service.userProfile;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.core.dtos.userProfile.UserProfileHomeDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileContractInfoDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileSmallDTO;
import it.palex.attendanceManagement.data.dto.transformers.UserProfileContractInfoTransformer;
import it.palex.attendanceManagement.data.dto.transformers.UserProfileTransformer;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.UserProfileContractInfo;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileContractInfoService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;

@Service
public class HomeWebService implements GenericService {

	@Autowired
	private UserProfileContractInfoService profileContractInfoService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	
	public GenericResponse<UserProfileHomeDTO> getUserCurrentLoggedProfileHome() {
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		UserProfileHomeDTO home = new UserProfileHomeDTO();
		
		
		UserProfileContractInfo contract = this.profileContractInfoService.findByUserProfile(profile);
		
		if(contract!=null) {
			UserProfileContractInfoDTO contractDTO = UserProfileContractInfoTransformer.mapToDTO(contract,
					profile.getDateOfEmployment(), false);
			
			home.setContractInfo(contractDTO);
		}
		
		UserProfileSmallDTO profileDTO = UserProfileTransformer.mapToSmallDTO(profile);
		home.setUserProfile(profileDTO);
		
		
		
		//TODO
		home.setLastUpdateDate(new Date());
	
		
		return this.buildOkResponse(home);
	}

	
	
	
	
}
