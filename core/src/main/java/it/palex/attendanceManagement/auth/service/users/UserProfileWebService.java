package it.palex.attendanceManagement.auth.service.users;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.palex.attendanceManagement.auth.dto.UpdateUserEmailDTO;
import it.palex.attendanceManagement.auth.dto.UpdateUserProfileDTO;
import it.palex.attendanceManagement.auth.dto.UpdateUserProfileSecondaryInfoDTO;
import it.palex.attendanceManagement.auth.dto.UserPersonalInfoDTO;
import it.palex.attendanceManagement.auth.dto.UserProfileAddressUpdateRequest;
import it.palex.attendanceManagement.auth.dto.UserProfileFilteringRequest;
import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.dto.core.UserProfileAddressDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileContractInfoDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileDTO;
import it.palex.attendanceManagement.data.dto.transformers.UserProfileAddressTransformer;
import it.palex.attendanceManagement.data.dto.transformers.UserProfileContractInfoTransformer;
import it.palex.attendanceManagement.data.dto.transformers.UserProfileTransformer;
import it.palex.attendanceManagement.data.entities.Company;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.Office;
import it.palex.attendanceManagement.data.entities.UserContacts;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.UserProfileAddress;
import it.palex.attendanceManagement.data.entities.UserProfileContractInfo;
import it.palex.attendanceManagement.data.entities.UserProfileImage;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.core.UserLevel;
import it.palex.attendanceManagement.data.entities.enumTypes.AddressTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.ContactTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.StandardDocumentDescription;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedImageCompression;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.core.CompanyService;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.core.UserProfileAddressService;
import it.palex.attendanceManagement.data.service.core.UserProfileImageService;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.data.service.impiegato.UserLevelService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.data.service.sede.OfficeService;
import it.palex.attendanceManagement.data.service.user.UserContactsService;
import it.palex.attendanceManagement.data.service.user.UsersAuthDetailsService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.GenericValidator;
import it.palex.attendanceManagement.library.utils.HttpCodes;

@Service
public class UserProfileWebService implements GenericService {
	
	@Autowired
	private UserProfileService userProfileSrv;
	
	@Autowired
	private UserProfileAddressService userProfileAddressService;
	
	@Autowired
	private GlobalConfigurationsService globalConfigurationService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	@Autowired
	private UserProfileImageService userProfileImageService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private UserContactsService userContactsService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private UserLevelService userLevelService;
		
	@Autowired
	private UsersAuthDetailsService usersAuthDetailsService;
	
	@Autowired
	private OfficeService officeService;
	
	public GenericResponse<Page<UserProfileDTO>> findAllUsers(UserProfileFilteringRequest filters, Pageable pageable) {
		if(filters==null || pageable==null) {
			return this.buildBadDataResponse();
		}
		
		List<UserProfile> res = this.userProfileSrv.findAll(filters.getId(), filters.getBirthDate(), filters.getCf(),
				filters.getEmail(), filters.getEmploymentDate(), filters.getLevel(), 
				filters.getName(), filters.getPhoneNumber(), filters.getSex(),
				filters.getSurname(), pageable);
		
		long totalCount = this.userProfileSrv.countAll(filters.getId(), filters.getBirthDate(), filters.getCf(),
				filters.getEmail(), filters.getEmploymentDate(), filters.getLevel(), 
				filters.getName(), filters.getPhoneNumber(), filters.getSex(),
				filters.getSurname());
		
		List<UserProfileDTO> list = UserProfileTransformer.mapToDTO(res);
		
		
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}


	public GenericResponse<UserPersonalInfoDTO> findUserDetails(Integer profileId, String imageCompression) {
		if(profileId==null || imageCompression==null || !SupportedImageCompression.isValid(imageCompression)) {
			return this.buildBadDataResponse();
		}
		UserProfile userProfile = this.userProfileSrv.findById(profileId);
		
		if(userProfile==null) {
			return this.buildNotFoundResponse();
		}
		
		UserPersonalInfoDTO profileDetails = this.buildUserInfo(userProfile, 
				SupportedImageCompression.valueOf(imageCompression));
		
		return this.buildOkResponse(profileDetails);
	}

	
	private UserPersonalInfoDTO buildUserInfo(UserProfile userProfile, SupportedImageCompression imageCompression) {
		UserProfileDTO userProfileDTO = this.userProfileSrv.buildUserProfileInfo(userProfile, imageCompression);
		
		UserProfileAddressDTO domicileDTO = this.userProfileAddressService
					.findByUserProfileAndAddressForWeb(userProfile, AddressTypeEnum.DOMICILE);
		
		UserProfileAddressDTO residenceDTO = this.userProfileAddressService
				.findByUserProfileAndAddressForWeb(userProfile, AddressTypeEnum.RESIDENCE);
		
		UserPersonalInfoDTO profileDetails = new UserPersonalInfoDTO();
		profileDetails.setDomicile(domicileDTO);
		profileDetails.setResidence(residenceDTO);
		profileDetails.setUserProfile(userProfileDTO);
		profileDetails.setContractInfo(UserProfileContractInfoTransformer.mapToDTO(
				userProfile.getUserProfileContractInfo(), userProfile.getDateOfEmployment()));
		
		return profileDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST})
	public GenericResponse<UserProfileAddressDTO> saveOrChangeUserAddress(
			UserProfileAddressUpdateRequest req, AddressTypeEnum addressType) {
		if(req==null || req.getUserProfileId()==null || addressType==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile userProfile = this.userProfileSrv.findById(req.getUserProfileId());
		
		if(userProfile==null) {
			return this.buildNotFoundResponse();
		}
		
		return updateUserProfileAddress(req, addressType, userProfile);
	}

	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST, HttpCodes.CONFLICT})
	public GenericResponse<UserProfileDTO> updateUserProfile(UpdateUserProfileDTO req,
			String imageCompression) {
		if(req==null || req.getUserProfileId()==null || imageCompression==null 
				|| !SupportedImageCompression.isValid(imageCompression) || req.getEmploymentDate()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile userProfile = this.userProfileSrv.findById(req.getUserProfileId());
				
		if(userProfile==null) {
			return this.buildNotFoundResponse();
		}
		
		return this.saveUserProfileData(req, userProfile, SupportedImageCompression.valueOf(imageCompression), false);
	}
	
	
	public GenericResponse<UserProfileDTO> getMyProfile(String imageCompression) {
		if(imageCompression==null || !SupportedImageCompression.isValid(imageCompression)) {
			return this.buildBadDataResponse();
		}
		UserProfile userProfile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(userProfile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		UserProfileDTO res = this.userProfileSrv
				.buildUserProfileInfo(userProfile, SupportedImageCompression.valueOf(imageCompression));
		
		return this.buildOkResponse(res);
	}

	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST, HttpCodes.CONFLICT})
	public GenericResponse<UserProfileDTO> updateMyProfile(UpdateUserProfileDTO req, String profileImageCompression) {
		if(req==null || profileImageCompression==null || !SupportedImageCompression.isValid(profileImageCompression)) {
			return this.buildBadDataResponse();
		}
		UserProfile userProfile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(userProfile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		return this.saveUserProfileData(req, userProfile, 
				SupportedImageCompression.valueOf(profileImageCompression), true);
	}


	private GenericResponse<UserProfileDTO> saveUserProfileData(UpdateUserProfileDTO req, UserProfile userProfile,
			SupportedImageCompression imageCompression, boolean calledByUser) {
		//cf is optional
		if(req.getCf()!=null && !GenericValidator.isAValidCodiceFiscale_NAIF_TEST(req.getCf())) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.INVALID_CF);
		}
		
		if(req.getPhoneNumber()!=null && !GenericValidator.isValidNumeroTelefono(req.getPhoneNumber())) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.INVALID_PHONE_NUMBER);
		}
		
		Date tomorrow = DateUtility.addDaysToDate(DateUtility.getCurrentDateInUTC(), 1);
		tomorrow = DateUtility.startOfDayOfDate(tomorrow);
		
		if(req.getBirthDate()==null || tomorrow.before(req.getBirthDate())) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.INVALID_BIRTH_DATE);
		}
		
		boolean mustCheckDuplicatedContact = this.globalConfigurationService.
				isEnabledUniqueContactForUserConfiguration();
		
		if(req.getPhoneNumber()!=null) {
			UserContacts numberContact = this.userContactsService.findByKey(userProfile.getId(), ContactTypeEnum.PHONE_NUMBER);
			
			if(mustCheckDuplicatedContact) {
				boolean alreadyExistsPhoneNumber = this.userContactsService
						.checkExistancefindByValueIgnoreCaseExcludingUser(req.getPhoneNumber(), 
								ContactTypeEnum.PHONE_NUMBER, userProfile);
				
				if (alreadyExistsPhoneNumber) {
					return this.buildConflictEntity(StandardReturnCodesEnum.PHONE_NUMBER_ALREADY_REGISTERED);
				}
			}
			
			
			if(numberContact==null) {
				numberContact = new UserContacts();
				numberContact.setCValue(req.getPhoneNumber());
				numberContact.setFkIdUsersAuthDetails(userProfile.getUsersAuthDetails());
				numberContact.setUserContactType(ContactTypeEnum.PHONE_NUMBER.name());
				numberContact.setVerified(false);
			}
			numberContact.setCValue(req.getPhoneNumber());
			
			numberContact = this.userContactsService.saveOrUpdate(numberContact);
		}
		
		if(!calledByUser) { //is the changes is not made by user itself
			userProfile.setSex(req.getSex());
			userProfile.setDateOfEmployment(req.getEmploymentDate());
		}
		
		userProfile.setName(req.getName());
		userProfile.setSurname(req.getSurname());
		userProfile.setBirthDate(req.getBirthDate());
		userProfile.setFiscalCode(req.getCf());
		userProfile.setPhoneNumber(req.getPhoneNumber());
		
		if(!userProfile.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse("Invalid Data");
		}
		
		userProfile = this.userProfileSrv.saveOrUpdate(userProfile, false);
		
		UserProfileDTO res = this.userProfileSrv.buildUserProfileInfo(userProfile, imageCompression);
		
		return this.buildOkResponse(res);
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST, HttpCodes.CONFLICT})	
	public GenericResponse<UserProfileDTO> updateUserEmail(UpdateUserEmailDTO req, 
			String profileImageCompression) {
		if(req==null || req.getEmail()==null || req.getUserProfileId()==null || 
				profileImageCompression==null || !SupportedImageCompression.isValid(profileImageCompression)) {
			return this.buildBadDataResponse();
		}
		
		if(!GenericValidator.isValidEmail(req.getEmail())) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.INVALID_EMAIL);
		}
		
		UserProfile userProfile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(userProfile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		String email = req.getEmail().trim().toLowerCase();
		
		
		
		UserProfile toUpdate = this.userProfileSrv.findById(req.getUserProfileId());
		
		if(toUpdate==null) {
			return this.buildNotFoundResponse();
		}
		
		//if are not equal you have to update the email
		if(!StringUtils.equalsIgnoreCase(toUpdate.getEmail(), email)) {
			
			
			UsersAuthDetails userCheck = this.usersAuthDetailsService.findByUsername(email);
			
			if (userCheck != null && !userCheck.getId().equals(toUpdate.getUsersAuthDetails().getId())) {
				return this.buildConflictEntity(StandardReturnCodesEnum.USERNAME_ALREADY_REGISTRED);
			}
			
			UsersAuthDetails user = toUpdate.getUsersAuthDetails();
			
			boolean mustCheckDuplicatedContact = this.globalConfigurationService.
					isEnabledUniqueContactForUserConfiguration();
			
			if(mustCheckDuplicatedContact) {
				boolean alreadyExistsEmail = this.userContactsService
						.checkExistancefindByValueIgnoreCase(email, ContactTypeEnum.EMAIL_ADDRESS);

				if (alreadyExistsEmail) {
					return this.buildConflictEntity(StandardReturnCodesEnum.MAIL_ALREADY_REGISTRED);
				}
			}
			
			UserContacts oldContact = this.userContactsService.findByKey(
					toUpdate.getUsersAuthDetails().getId(), ContactTypeEnum.EMAIL_ADDRESS);
			
			if(oldContact==null) {
				oldContact = new UserContacts();
				oldContact.setCValue(email);
				oldContact.setFkIdUsersAuthDetails(user);
				oldContact.setUserContactType(ContactTypeEnum.EMAIL_ADDRESS.name());
				oldContact.setVerified(false);
				oldContact.setVerificationToken(null);
				oldContact.setVerificationTokenCreationDate(null);
				oldContact.setVerificationTokenExpirationDate(null);
			}else {
				oldContact.setCValue(email);
			}
			
			
			user.setUsername(email);
			userProfile.setEmail(email);
			
			
			if(!oldContact.canBeInsertedInDatabase()) {
				return this.buildBadDataResponse();
			}
			
			if(!user.canBeInsertedInDatabase()) {
				return this.buildBadDataResponse("Invalid Data");
			}
			
			if(!userProfile.canBeInsertedInDatabase()) {
				return this.buildBadDataResponse("Invalid Data");
			}
			
			user = this.usersAuthDetailsService.saveOrUpdate(user);
			
			userProfile = this.userProfileSrv.saveOrUpdate(userProfile, false);
			
			oldContact = this.userContactsService.saveOrUpdate(oldContact);
		}
		

		UserProfileDTO res = this.userProfileSrv.buildUserProfileInfo(userProfile, 
				SupportedImageCompression.valueOf(profileImageCompression));
		
		return this.buildOkResponse(res);
	}
	


	public GenericResponse<UserPersonalInfoDTO> retriveMyProfileInfo(String profileImageCompression) {
		if(profileImageCompression==null || !SupportedImageCompression.isValid(profileImageCompression)) {
			return this.buildBadDataResponse();
		}
		UserProfile userProfile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(userProfile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		UserPersonalInfoDTO profileDetails = this.buildUserInfo(userProfile, 
				SupportedImageCompression.valueOf(profileImageCompression));
		
		
		return this.buildOkResponse(profileDetails);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST})
	public GenericResponse<UserProfileAddressDTO> saveOrChangeMyAddress(UserProfileAddressUpdateRequest req, AddressTypeEnum addressType){
		if(req==null || addressType==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile userProfile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(userProfile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		return updateUserProfileAddress(req, addressType, userProfile);
	}


	private GenericResponse<UserProfileAddressDTO> updateUserProfileAddress(UserProfileAddressUpdateRequest req,
			AddressTypeEnum addressType, UserProfile userProfile) {
		UserProfileAddress address = this.userProfileAddressService.
				findByUserProfileAndAddress(userProfile, addressType);
		
		if(address==null) {
			address = new UserProfileAddress();
		}
		
		address.setAddressType(addressType.name());
		address.setCity(req.getCity());
		address.setNation(req.getNation());
		address.setProvince(req.getProvince());
		address.setStreet(req.getStreet());
		address.setUserProfile(userProfile);
		address.setZipCode(req.getZipCode());
		
		if(!address.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		address = this.userProfileAddressService.saveOrUpdate(address);
		
		
		return this.buildOkResponse(UserProfileAddressTransformer.mapToDTO(address));
	}

	
	@Transactional(rollbackFor = {Exception.class})
	public GenericResponse<StringDTO> uploadImageProfile(MultipartFile file) throws Exception {
		if(file==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile userProfile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if (userProfile == null) {
			return buildUnauthorizedResponse();
		}
		
		Document document = this.documentService.saveImageWithDefaultFM(file.getOriginalFilename(),
				file.getInputStream(), userProfile.getId()+"", StandardDocumentDescription.PROFILE_IMAGE.name());
		
		this.userProfileImageService.deleteAllImageWithCompressionDifferentFrom(userProfile,
				SupportedImageCompression.NORMAL);
		
		UserProfileImage newImage = this.userProfileImageService.saveOrUpdateImageProfile(userProfile, document, 
				SupportedImageCompression.NORMAL);
		
		StringDTO dto = new StringDTO();
		dto.setValue(newImage.getDownloadToken());

		return this.buildOkResponse(dto);
	}

	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST})
	public GenericResponse<UserProfileDTO> updateOtherProfileInfo(UpdateUserProfileSecondaryInfoDTO req,
			String imageCompression) {
		if(req==null || req.getUserProfileId()==null || imageCompression==null 
				|| !SupportedImageCompression.isValid(imageCompression) || req.getCompanyId()==null) {
			return this.buildBadDataResponse();
		}
		
		if(req.getLevelId()==null || req.getWorkDayHours()==null || req.getWorkDayHours()<=0
				|| req.getWorkDayHours()>24 || req.getLeaveHours()==null 
					|| req.getVacationDays()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile userProfile = this.userProfileSrv.findById(req.getUserProfileId());
				
		if(userProfile==null) {
			return this.buildNotFoundResponse();
		}
		
		//check existance of company id
		Company company = this.companyService.findById(req.getCompanyId());
		
		if(company==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.COMPANY_NOT_EXISTS);
		}
		
		UserProfileContractInfo contractInfo = userProfile.getUserProfileContractInfo();
		if(contractInfo==null) {
			contractInfo = new UserProfileContractInfo();
		}
		
		UserLevel level = this.userLevelService.findById(req.getLevelId());
		
		if(level==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.USER_LEVEL_NOT_FOUND);
		}
		
		Office employmentOffice = null;
		
		if(req.getEmploymentOffice()!=null) {
			employmentOffice = this.officeService.getByOfficeName(req.getEmploymentOffice());
			
			if(employmentOffice==null) {
				return this.buildNotFoundResponse(StandardReturnCodesEnum.NOT_FOUND_EMPLOYMENT_OFFICE);
			}
		}
		
		contractInfo.setResidualLeaveHours(req.getLeaveHours());
		contractInfo.setResidualVacationDays(req.getVacationDays());
		contractInfo.setLevel(level);
		contractInfo.setWorkDayHours(req.getWorkDayHours());
		contractInfo.setUserProfile(userProfile);
		contractInfo.setEmploymentOffice(employmentOffice);
		
		userProfile.setCompany(company);
		userProfile.setUserProfileContractInfo(contractInfo);
		
		userProfile = this.userProfileSrv.saveOrUpdate(userProfile, false);
		
		if(!userProfile.canBeInsertedInDatabase() || !contractInfo.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
				
		UserProfileDTO res = this.userProfileSrv.buildUserProfileInfo(userProfile, 
				SupportedImageCompression.valueOf(imageCompression));
		
		return this.buildOkResponse(res);
	}


	public GenericResponse<UserProfileContractInfoDTO> retrieveUserProfileContractInfo() {
		UserProfile userProfile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(userProfile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		UserProfileContractInfoDTO res = UserProfileContractInfoTransformer.mapToDTO(
				userProfile.getUserProfileContractInfo(), userProfile.getDateOfEmployment());
		
		
		return this.buildOkResponse(res);
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<UserProfileDTO> disableUserProfile(Integer userProfileId) {
		return disableEnableProfile(userProfileId, false);
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<UserProfileDTO> enableUserProfile(Integer userProfileId) {
		return disableEnableProfile(userProfileId, true);
	}
	
	
	
	private GenericResponse<UserProfileDTO> disableEnableProfile(Integer userProfileId, boolean enabled){
		UserProfile userProfile = this.userProfileSrv.findById(userProfileId);
		
		if(userProfile==null) {
			return this.buildNotFoundResponse();
		}
		
		UserProfile currentUser = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		if(currentUser.getId().equals(userProfile.getId())) {
			
		}
		
		if(currentUser.getId().equals(userProfile.getId())) {
			return this.buildNotAcceptableResponse(StandardReturnCodesEnum.USER_CANNOT_DISABLE_ENABLE_HIMSELF);
		}
		
		UsersAuthDetails user = userProfile.getUsersAuthDetails();
		user.setIsEnabled(enabled);
		this.usersAuthDetailsService.saveOrUpdate(user);
		
		return this.buildOkResponse(UserProfileTransformer.mapToDTO(userProfile));
	}


	

}
