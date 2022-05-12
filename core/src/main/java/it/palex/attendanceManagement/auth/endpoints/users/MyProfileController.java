package it.palex.attendanceManagement.auth.endpoints.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import it.palex.attendanceManagement.auth.dto.UpdateUserProfileDTO;
import it.palex.attendanceManagement.auth.dto.UserPersonalInfoDTO;
import it.palex.attendanceManagement.auth.dto.UserProfileAddressUpdateRequest;
import it.palex.attendanceManagement.auth.service.users.UserProfileWebService;
import it.palex.attendanceManagement.data.dto.core.UserProfileAddressDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileContractInfoDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileDTO;
import it.palex.attendanceManagement.data.dto.documents.DocumentoReadInternalResponse;
import it.palex.attendanceManagement.data.entities.enumTypes.AddressTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedImageCompression;
import it.palex.attendanceManagement.data.service.core.UserProfileImageService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.HttpCodes;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping("/myProfile")
public class MyProfileController extends RestEndpoint {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MyProfileController.class);
			
	@Autowired
	private UserProfileWebService userProfileWebService;
	
	@Autowired
	private UserProfileImageService userProfileImageService;
	
	
	
	@GetMapping()
	@PreAuthorize("hasAuthority('LOGGED_USER_PERMISSION')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileDTO>> retrieveProfileDetails(
    		@RequestParam(name="profileImageCompression", required=true, defaultValue = SupportedImageCompression.SMALL_STR) String profileImageCompression){
	     
    	GenericResponse<UserProfileDTO>	res = this.userProfileWebService.getMyProfile(profileImageCompression);
    	
    	return this.buildGenericResponse(res);	
	}
	
	@PostMapping()
	@PreAuthorize("hasAuthority('LOGGED_USER_PERMISSION')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileDTO>> updateMyProfile(
    		@RequestBody UpdateUserProfileDTO req,
    		@RequestParam(name="profileImageCompression", required=true, defaultValue = SupportedImageCompression.SMALL_STR) String profileImageCompression){
	     
    	GenericResponse<UserProfileDTO>	res = this.userProfileWebService
    			.updateMyProfile(req, profileImageCompression);
    	
    	return this.buildGenericResponse(res);	
	}
	
	
	@GetMapping("allPersonalData")
	@PreAuthorize("hasAuthority('LOGGED_USER_PERMISSION')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserPersonalInfoDTO>> retrieveAllPersonalInformationProfileDetails(
    		@RequestParam(name="profileImageCompression", required=true, defaultValue = SupportedImageCompression.SMALL_STR) String profileImageCompression){
	     
    	GenericResponse<UserPersonalInfoDTO> res = this.userProfileWebService.retriveMyProfileInfo(profileImageCompression);
    	
    	return this.buildGenericResponse(res);	
	}
	
	@GetMapping("myContractDetails")
	@PreAuthorize("hasAuthority('LOGGED_USER_PERMISSION')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileContractInfoDTO>> retrieveUserProfileContractInfo(){
	     
    	GenericResponse<UserProfileContractInfoDTO> res = 
    			this.userProfileWebService.retrieveUserProfileContractInfo(false);
    	
    	return this.buildGenericResponse(res);	
	}
	
	
	@PostMapping("updateMyDomicileAddress")
	@PreAuthorize("hasAuthority('LOGGED_USER_PERMISSION')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileAddressDTO>> saveOrChangeMyAddressDomicile(
    		@RequestBody UserProfileAddressUpdateRequest userProfileAddress){
	     
    	GenericResponse<UserProfileAddressDTO> res = this.userProfileWebService.saveOrChangeMyAddress(
    			userProfileAddress, AddressTypeEnum.DOMICILE);
    	
    	return this.buildGenericResponse(res);	
	}
	
	@PostMapping("updateMyResidenceAddress")
	@PreAuthorize("hasAuthority('LOGGED_USER_PERMISSION')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<UserProfileAddressDTO>> saveOrChangeMyAddressResidence(
    		@RequestBody UserProfileAddressUpdateRequest userProfileAddress){
	     
    	GenericResponse<UserProfileAddressDTO> res = this.userProfileWebService.saveOrChangeMyAddress(
    			userProfileAddress, AddressTypeEnum.RESIDENCE);
    	
    	return this.buildGenericResponse(res);	
	}
	
	
	@PostMapping("/image")
    @Loggable()
    @PreAuthorize("hasAuthority('LOGGED_USER_PERMISSION')")
    public ResponseEntity<GenericResponse<StringDTO>> uploadImageProfile(
    		@RequestPart("image") MultipartFile file) throws Exception {
        GenericResponse<StringDTO> response = this.userProfileWebService.uploadImageProfile(file);
        
        return this.buildGenericResponse(response);
    }
	
	@GetMapping(value = "/downloadProfileImage", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<StreamingResponseBody> openStreamToDownloadProfileImage(
			@RequestParam(name="downloadToken", required = true) String downloadToken) {
		DocumentoReadInternalResponse response = null;
		
		try {
			response = this.userProfileImageService
					.openStreamOnUserProfileImage(downloadToken);
			
			if(response==null) {
				return ResponseEntity.notFound().build();
			}
			
			return ResponseEntity
	                .ok()
	                .header("content-disposition","attachment; filename = "+response.getFileName())
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(response.getResponseBody());
			
		}catch(Exception e) {
			LOGGER.error("error during document retrive", e);
			return ResponseEntity.status(HttpCodes.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
}

