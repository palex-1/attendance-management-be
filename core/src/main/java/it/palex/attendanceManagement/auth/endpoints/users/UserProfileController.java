package it.palex.attendanceManagement.auth.endpoints.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.auth.dto.UpdateUserEmailDTO;
import it.palex.attendanceManagement.auth.dto.UpdateUserProfileDTO;
import it.palex.attendanceManagement.auth.dto.UpdateUserProfileSecondaryInfoDTO;
import it.palex.attendanceManagement.auth.dto.UserPersonalInfoDTO;
import it.palex.attendanceManagement.auth.dto.UserProfileAddressUpdateRequest;
import it.palex.attendanceManagement.auth.dto.UserProfileFilteringRequest;
import it.palex.attendanceManagement.auth.service.users.UserProfileWebService;
import it.palex.attendanceManagement.data.dto.core.UserProfileAddressDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AddressTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedImageCompression;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping("/users")
public class UserProfileController extends RestEndpoint {

	@Autowired
	private UserProfileWebService userProfileWebService;
	
	
	@GetMapping(path = "/findAll")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<UserProfileDTO>>> findAll(
			UserProfileFilteringRequest filters, 
			@PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.ASC) Pageable pageable) {
		GenericResponse<Page<UserProfileDTO>> res = this.userProfileWebService
				.findAllUsers(filters, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path = "/findUserDetails")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<UserPersonalInfoDTO>> findUserDetails(
			@RequestParam(name = "profileId", required = true) Integer profileId,
			@RequestParam(name="profileImageCompression", required=true, defaultValue = SupportedImageCompression.SMALL_STR) String profileImageCompression) {
		
		GenericResponse<UserPersonalInfoDTO> res = this.userProfileWebService.findUserDetails(profileId, profileImageCompression);
		
		return this.buildGenericResponse(res);
	}
	
	@PostMapping("updateUserEmail")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileDTO>> updateUserEmail(
    		@RequestBody UpdateUserEmailDTO req,
    		@RequestParam(name="profileImageCompression", required=true, defaultValue = SupportedImageCompression.SMALL_STR) String profileImageCompression){
	     
    	GenericResponse<UserProfileDTO>	res = this.userProfileWebService
    			.updateUserEmail(req, profileImageCompression);
    	
    	return this.buildGenericResponse(res);	
	}
	
	@PostMapping("updateProfile")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileDTO>> updateProfile(
    		@RequestBody UpdateUserProfileDTO req,
    		@RequestParam(name="profileImageCompression", required=true, defaultValue = SupportedImageCompression.SMALL_STR) String profileImageCompression){
	     
    	GenericResponse<UserProfileDTO>	res = this.userProfileWebService
    			.updateUserProfile(req, profileImageCompression);
    	
    	return this.buildGenericResponse(res);	
	}
	
	@PostMapping("updateOtherProfileInfo")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<UserProfileDTO>> updateOtherProfileInfo(
    		@RequestBody UpdateUserProfileSecondaryInfoDTO req,
    		@RequestParam(name="profileImageCompression", required=true, defaultValue = SupportedImageCompression.SMALL_STR) String profileImageCompression){
	     
    	GenericResponse<UserProfileDTO>	res = this.userProfileWebService
    			.updateOtherProfileInfo(req, profileImageCompression);
    	
    	return this.buildGenericResponse(res);	
	}
	
	@PostMapping("updateDomicileAddress")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileAddressDTO>> saveOrChangeAddressDomicile(
    		@RequestBody UserProfileAddressUpdateRequest userProfileAddress){
	     
    	GenericResponse<UserProfileAddressDTO> res = this.userProfileWebService.saveOrChangeUserAddress(
    			userProfileAddress, AddressTypeEnum.DOMICILE);
    	
    	return this.buildGenericResponse(res);	
	}
	
	@PostMapping("updateResidenceAddress")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileAddressDTO>> saveOrChangeAddressResidence(
    		@RequestBody UserProfileAddressUpdateRequest userProfileAddress){
	     
    	GenericResponse<UserProfileAddressDTO> res = this.userProfileWebService.saveOrChangeUserAddress(
    			userProfileAddress, AddressTypeEnum.RESIDENCE);
    	
    	return this.buildGenericResponse(res);	
	}
	
	
	
}
