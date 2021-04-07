package it.palex.attendanceManagement.auth.service.registration;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.auth.dto.UserProfileCreationRequestDTO;
import it.palex.attendanceManagement.commons.messaging.email.CompileAndSendEmailService;
import it.palex.attendanceManagement.commons.security.CustomAuthenticationProvider;
import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.dto.auth.PermissionGroupLabelDTO;
import it.palex.attendanceManagement.data.dto.core.CompanyDTO;
import it.palex.attendanceManagement.data.dto.core.UserLevelDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileDTO;
import it.palex.attendanceManagement.data.dto.transformers.CompanyTransformer;
import it.palex.attendanceManagement.data.dto.transformers.PermissionGroupLabelTransformer;
import it.palex.attendanceManagement.data.dto.transformers.UserLevelTransformer;
import it.palex.attendanceManagement.data.dto.transformers.UserProfileTransformer;
import it.palex.attendanceManagement.data.entities.Company;
import it.palex.attendanceManagement.data.entities.Office;
import it.palex.attendanceManagement.data.entities.UserContacts;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.UserProfileContractInfo;
import it.palex.attendanceManagement.data.entities.auth.PermissionGroupLabel;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.core.SupportedLangI18n;
import it.palex.attendanceManagement.data.entities.core.UserLevel;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthProvidersEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.data.entities.enumTypes.ContactTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.PermissionGroupsValues;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedLangsEnumI18N;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.auth.PermissionGroupLabelService;
import it.palex.attendanceManagement.data.service.core.CompanyService;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.core.SupportedLangI18nService;
import it.palex.attendanceManagement.data.service.impiegato.UserLevelService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.data.service.sede.OfficeService;
import it.palex.attendanceManagement.data.service.user.UserContactsService;
import it.palex.attendanceManagement.data.service.user.UsersAuthDetailsService;
import it.palex.attendanceManagement.library.exception.BadDataException;
import it.palex.attendanceManagement.library.exception.InternalServerErrorException;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.GenericValidator;
import it.palex.attendanceManagement.library.utils.HttpCodes;
import it.palex.attendanceManagement.library.utils.StringUtility;
import it.palex.attendanceManagement.library.utils.crypto.SecureStringGenerator;

@Component
public class RegistrationWebService implements GenericService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RegistrationWebService.class);
			
	private static final int LENGTH_OF_INITIAL_PASSWORD = 16;
	
	@Autowired
	private UsersAuthDetailsService userAuthDetailsService;
	
	@Autowired
	private UserProfileService userProfileSrv;

	@Autowired
	private UserContactsService userContactSrv;
	
	@Autowired
	private PermissionInitializerService permissionInitializerService;
		
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CustomAuthenticationProvider authenticationManager;
	
	@Autowired
	private CompileAndSendEmailService compileAndSendEmailService;
	
	@Autowired
	private PermissionGroupLabelService permissionGroupLabelService;
	
	@Autowired
	private SupportedLangI18nService supportedLangI18nService;
	
	@Autowired
	private UserLevelService userLevelService;
	
	@Autowired
	private GlobalConfigurationsService globalConfigurationService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	@Autowired
	private OfficeService officeService;
	
	
	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = { HttpCodes.BAD_REQUEST, 
			HttpCodes.INTERNAL_SERVER_ERROR, HttpCodes.UNPROCESSABLE_ENTITY })
	public GenericResponse<UserProfileDTO> createNewUser(UserProfileCreationRequestDTO toAdd) {
		if (toAdd == null || StringUtility.isEmptyOrWhitespace(toAdd.getUsername())
				|| StringUtility.isEmptyOrWhitespace(toAdd.getEmail())
					|| StringUtility.isEmptyOrWhitespace(toAdd.getName())
						|| StringUtility.isEmptyOrWhitespace(toAdd.getSurname())
							|| StringUtility.isEmptyOrWhitespace(toAdd.getSex())) {
			return this.buildBadDataResponse();
		}
		
		
		String email = toAdd.getEmail().trim().toLowerCase();
		
		if(toAdd.getPermissionGroupLabelId()==null || toAdd.getBirthDate()==null 
				|| toAdd.getDateOfEmployment()==null || toAdd.getCompanyId()==null
				 ) {
			return this.buildBadDataResponse();
		}
		
		if(toAdd.getWorkDayHours()==null || toAdd.getWorkDayHours()<=0 || 
			toAdd.getWorkDayHours()>24 || toAdd.getInitialVacationDays()==null || 
			  toAdd.getInitialLeaveHours()==null ||  toAdd.getLevelId()==null) {
			return this.buildBadDataResponse("Invalid contract info");
		}

		Date currentDate = DateUtility.getCurrentDateInUTC();
		
		if(toAdd.getBirthDate().after(currentDate)) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.INVALID_BIRTH_DATE);
		}
		
		UserLevel level = this.userLevelService.findById(toAdd.getLevelId());
		if(level==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.USER_LEVEL_NOT_FOUND);
		}
		
		
		if(!GenericValidator.isValidEmail(email)) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.INVALID_EMAIL);
		}
		
		if(toAdd.getPhoneNumber()!=null && !GenericValidator.isValidNumeroTelefono(toAdd.getPhoneNumber())) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.INVALID_PHONE_NUMBER);
		}
		
		UsersAuthDetails user = this.userAuthDetailsService.findByUsername(toAdd.getUsername());
		if (user != null) {
			return this.buildConflictEntity(StandardReturnCodesEnum.USERNAME_ALREADY_REGISTRED);
		}
		
		boolean mustCheckDuplicatedContact = this.globalConfigurationService.
				isEnabledUniqueContactForUserConfiguration();
		
		if(mustCheckDuplicatedContact) {
			boolean alreadyExistsEmail = this.userContactSrv
					.checkExistancefindByValueIgnoreCase(email, ContactTypeEnum.EMAIL_ADDRESS);

			if (alreadyExistsEmail) {
				return this.buildConflictEntity(StandardReturnCodesEnum.MAIL_ALREADY_REGISTRED);
			}
		}
		
		
		if(toAdd.getPhoneNumber()!=null && mustCheckDuplicatedContact) {
			boolean alreadyExistsPhoneNumber = this.userContactSrv
					.checkExistancefindByValueIgnoreCase(toAdd.getPhoneNumber(), ContactTypeEnum.PHONE_NUMBER);
			
			if (alreadyExistsPhoneNumber) {
				return this.buildConflictEntity(StandardReturnCodesEnum.PHONE_NUMBER_ALREADY_REGISTERED);
			}
		}
		
		//check existance of permission group id
		PermissionGroupLabel group = this.permissionGroupLabelService.findById(toAdd.getPermissionGroupLabelId());
		
		if(group==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.PERMISSION_GROUP_NOT_EXISTS);
		}
		
		//if you are tring to add an admin check that the user that add the admin is an admin
		if(group.getName().equals(PermissionGroupsValues.ADMINISTRATION)) {
			
			final String authoritiesToHave = AuthoritiesValues.ADMINISTRATION_PERMISSION;
			boolean hasAdminPerm = this.currentAuthenticatedUserService.currentUserHasAuthority(authoritiesToHave);
			
			if(!hasAdminPerm) {
				return this.buildForbiddenResponse(StandardReturnCodesEnum.ONLY_ADMIN_CAN_ADD_ADMIN);
			}
		}
		
		
		//check existance of company id
		Company company = this.companyService.findById(toAdd.getCompanyId());
		
		if(company==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.COMPANY_NOT_EXISTS);
		}
		
		final String tempPassword = SecureStringGenerator.getInstance()
				.generateSecureCharacterAndNumeric(LENGTH_OF_INITIAL_PASSWORD, true);
		
		Office employmentOffice = null;
		
		if(toAdd.getEmploymentOffice()!=null) {
			employmentOffice = this.officeService.getByOfficeName(toAdd.getEmploymentOffice());
			
			if(employmentOffice==null) {
				return this.buildNotFoundResponse(StandardReturnCodesEnum.NOT_FOUND_EMPLOYMENT_OFFICE);
			}
		}
		
		UsersAuthDetails newUser = this.createUserAuthDetails(toAdd.getUsername(), tempPassword, group);
		
		this.saveUserEmail(email, newUser);
		
		if(toAdd.getPhoneNumber()!=null) {
			this.saveUserPhoneNumber(toAdd.getPhoneNumber(), newUser);
		}
		
		SupportedLangI18n preferredLang = this.supportedLangI18nService.findByKey(SupportedLangsEnumI18N.DEFAULT_USER_LANG());

		//add to user all permissions
		this.permissionInitializerService.addPermissionToUserByPermissionGroup(newUser, group);
						
		UserProfile userProfile = new UserProfile();
		userProfile.setBirthDate(toAdd.getBirthDate());
		userProfile.setName(StringUtils.trim(toAdd.getName()));
		userProfile.setSurname(StringUtils.trim(toAdd.getSurname()));
		userProfile.setEmail(email);
		userProfile.setPhoneNumber(toAdd.getPhoneNumber());
		userProfile.setSex(StringUtils.trim(toAdd.getSex()));
		userProfile.setDateOfEmployment(toAdd.getDateOfEmployment());
		userProfile.setCompany(company);
		userProfile.setFiscalCode(StringUtils.trim(toAdd.getFiscalCode()));
		userProfile.setUsersAuthDetails(newUser);
		userProfile.setPreferredLang(preferredLang);
		
		UserProfileContractInfo contractInfo = new UserProfileContractInfo();
		
		contractInfo.setLevel(level);
		contractInfo.setUserProfile(userProfile);
		contractInfo.setWorkDayHours(toAdd.getWorkDayHours());
		contractInfo.setResidualVacationDays(toAdd.getInitialVacationDays());
		contractInfo.setResidualLeaveHours(toAdd.getInitialLeaveHours());
		contractInfo.setEmploymentOffice(employmentOffice);
		
		userProfile.setUserProfileContractInfo(contractInfo);
		
		if(!userProfile.canBeInsertedInDatabase() || !contractInfo.canBeInsertedInDatabase()) {
			throw new BadDataException("Cannot create user details");
		}
		
		userProfile = this.userProfileSrv.saveOrUpdate(userProfile, true);
		
		
		try {
			this.compileAndSendEmailService.sendInvitationEmail(email, toAdd.getUsername(), 
					tempPassword, SupportedLangsEnumI18N.DEFAULT_USER_LANG());
		} catch (Exception e) {
			LOGGER.error("", e);
			throw new InternalServerErrorException(StandardReturnCodesEnum.ERROR_SENDING_EMAIL_PLEASE_CHECK_IT);
		}
		
		UserProfileDTO res = UserProfileTransformer.mapToDTO(userProfile);
		
		return this.buildOkResponse(res);
	}
	
	
	private UsersAuthDetails createUserAuthDetails(String username, String password, PermissionGroupLabel permissionGroup) {
		UsersAuthDetails newUser = new UsersAuthDetails();
		newUser.setTwoFaEnabled(false);
		final String passwordHash = authenticationManager.hashPassword(password);
		if(!authenticationManager.checkBCryptPassword(password, passwordHash)) {
			throw new InternalServerErrorException("Error during Registration");
		}
		newUser.setHashedPassword(passwordHash);
		newUser.setIsAccountNonExpired(true);
		newUser.setIsAccountNonLocked(true);
		newUser.setIsCredentialsNonExpired(true);
		newUser.setIsEnabled(true);
		newUser.setLastPasswordChangeDate(DateUtility.getCurrentDateInUTC());
		newUser.setRegisteredWith(AuthProvidersEnum.LOCAL.name());
		newUser.setUsername(username);
		newUser.setMustResetPassword(true);
		newUser.setPermissionGroup(permissionGroup);
		
		if(!newUser.canBeInsertedInDatabase()) {
			throw new BadDataException("Cannot create user details");
		}
		
		return this.userAuthDetailsService.saveOrUpdate(newUser);
	}
	
	private UserContacts saveUserEmail(String email, UsersAuthDetails newUser) {
		return saveUserContacts(email, newUser, ContactTypeEnum.EMAIL_ADDRESS);
	}
	
	private UserContacts saveUserPhoneNumber(String phoneNumber, UsersAuthDetails newUser) {
		return saveUserContacts(phoneNumber, newUser, ContactTypeEnum.PHONE_NUMBER);
	}
	
	private UserContacts saveUserContacts(String value, UsersAuthDetails newUser, ContactTypeEnum type) {
		UserContacts userContact = new UserContacts();
		userContact.setCValue(value);
		userContact.setFkIdUsersAuthDetails(newUser);
		userContact.setUserContactType(type.name());
		userContact.setVerified(false);
		userContact.setVerificationToken(null);
		userContact.setVerificationTokenCreationDate(null);
		userContact.setVerificationTokenExpirationDate(null);
		
		
		if(!userContact.canBeInsertedInDatabase()) {
			throw new BadDataException("Cannot create contact "+type);
		}
		
		return this.userContactSrv.saveOrUpdate(userContact);
	}


	public GenericResponse<List<PermissionGroupLabelDTO>> findAllPermissionGroupLabels() {
		Sort sort = Sort.by(Direction.ASC, "name");
		
		List<PermissionGroupLabel> list = this.permissionGroupLabelService.findAll(sort);
		
		List<PermissionGroupLabelDTO> res = PermissionGroupLabelTransformer.mapToDTO(list);
		
		return this.buildOkResponse(res);
	}


	public GenericResponse<List<CompanyDTO>> findAllCompanies() {
		Sort sort = Sort.by(Direction.ASC, "name");
		
		List<Company> companies = this.companyService.findAll(sort);
		
		List<CompanyDTO> res = CompanyTransformer.mapToDTO(companies);
		
		return this.buildOkResponse(res);
	}


	public GenericResponse<List<UserLevelDTO>> findAllUserLevels() {
		List<UserLevel> levels = this.userLevelService.findAll();
		
		List<UserLevelDTO> res = UserLevelTransformer.mapToDTO(levels);
		
		return this.buildOkResponse(res);
	}
	
	
	
	
	
	
}
