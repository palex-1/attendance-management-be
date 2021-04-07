package it.palex.attendanceManagement.data.service.impiegato;

import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.data.entities.auth.QChangeEmailRequest;
import it.palex.attendanceManagement.library.service.GenericService;

/**
 * @author Alessandro Pagliaro
 *
 */
@Service
public class ChangeEmailRequestService implements GenericService{

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ChangeEmailRequestService.class);
	
	private final QChangeEmailRequest QCER = QChangeEmailRequest.changeEmailRequest;
	
//	@Autowired
//	private ChangeEmailRequestRepository changeEmailRepo;
//	
//	@Autowired
//	private ConfigurazioniService configSrv;
//	
//	@Autowired
//	private ConfigurazioniGlobaliService configGlobaliSrv;
//	
//	@Autowired
//	private ImpiegatoService impiegatoSrv;
//	
//	@Autowired
//	private ImpiegatoRepository impiegatoRepo;
//	
//	@Autowired
//	private EmailSenderService emailSenderSrv;
//	
//	/**
//	 * @param userAgent
//	 * @param httpRequest
//	 * @param changeEmail
//	 */
//	@Transactional(rollbackFor=Exception.class)
//	public GenericResponse<StringDTO> addEmailChangeRequest(String hashedUsername, String userAgent, HttpServletRequest httpRequest, String changeEmail) {
//		if(hashedUsername==null){
//			return this.buildUnauthorizedResponse();
//		}
//		if(changeEmail==null){
//			return this.buildBadDataResponse("Not valid email");
//		}
//		changeEmail = changeEmail.trim();
//		if(!GenericValidator.validateEmail(changeEmail)){
//			return this.buildBadDataResponse("Not valid email");
//		}
//		Impiegato impiegato = this.impiegatoSrv.findImpiegatoWithHashedUsername(hashedUsername);
//		if(impiegato==null){
//			return this.buildNotFoundResponse("Impiegato not found");
//		}
//		
//		if(StringUtils.equalsIgnoreCase(impiegato.getEmail(), changeEmail)){
//			return this.buildUnprocessableEntity("This is your current email");
//		}
//		
//		if(this.impiegatoSrv.checkExistanceOfImpiegatoByEmail(changeEmail)){
//			return this.buildUnprocessableEntity("This email is currently owned by another user");
//		}
//		String ip = InformationHttpRequestExtractor.getIpFromRequest(httpRequest, this.configSrv.getProxyType());
//
//		if(ip==null || userAgent==null || changeEmail==null ||  httpRequest==null){
//			return this.buildBadDataResponse();
//		}
//		String token = TokenGenerator.generateSecureTokenOf128Characters();
//		Date actualDateInUTC = DateUtility.getCurrentDateInUTC();
//		String newEmail = changeEmail.trim();
//		ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest(ip, userAgent, actualDateInUTC, 
//				newEmail, token, impiegato);
//		
//		if(!changeEmailRequest.canBeInsertedInDatabase()){
//			return this.buildBadDataResponse();
//		}
//		try{
//			sendChangePasswordConfirmationEmail(newEmail, token);
//		}catch(SendFailedException | AddressException e){
//			LOGGER.error("", e);
//			return this.buildInternalServerError("Recipient not found");
//		}catch(MessagingException e){
//			LOGGER.error("", e);
//			return this.buildInternalServerError("Error sending confirmation email");
//		}catch(Exception e){
//			LOGGER.error("", e);
//			return this.buildInternalServerError(e.getMessage());
//		}
//		this.changeEmailRepo.save(changeEmailRequest);
//		
//		return this.buildOkResponse(new StringDTO("A change email confirmation was sent to your new email"));
//	}
//	
//	
//	public void sendChangePasswordConfirmationEmail(String email, String token) throws Exception{
//		String frontendBaseAddress = this.configSrv.getFrontendBaseUrl();
//		String changeEmailAddress = this.configSrv.getChangeEmailRecoveryAddress();
//		
//		String address = AddressUtility.concatInternetAddress(frontendBaseAddress, changeEmailAddress);
//		address = AddressUtility.concatInternetAddress(address, token);
//		
//		MailBuilder builder = new EmailChangeConfirmationBuilder(this.configGlobaliSrv, address);
//		
//		this.emailSenderSrv.sendEmail(address, email, builder);
//	}
//	
//	public Properties buildEmailProperties(Etichetta etichetta){
//		return MailBuilder.buildEmailProperties(etichetta, this.configGlobaliSrv);
//	}
//	
//	
//	/**
//	 * @param authorizationToken
//	 * @return
//	 */
//	@Transactional(rollbackFor=Exception.class)
//	public GenericResponse<StringDTO> confirmEmailChangeRequest(String authorizationToken) {
//		BooleanBuilder condition = new BooleanBuilder();
//		condition.and(QCER.token.eq(authorizationToken));
//		
//		Iterator<ChangeEmailRequest> it = this.changeEmailRepo.findAll(condition).iterator();
//		if(!it.hasNext()){
//			return this.buildForbiddenResponse("token is invalid");
//		}
//		ChangeEmailRequest request = it.next();
//		Date creationDate = request.getCreationDate();
//		int seconds = this.configSrv.getValidityOfChangeEmailRequestInSeconds();
//		Date currentDate = DateUtility.getCurrentDateInUTC();
//		if(currentDate.after(DateUtility.addSecondsToDate(creationDate, seconds))){
//			return this.buildForbiddenResponse("token is expired");
//		}
//		String newEmail = request.getNewEmail();
//		if(this.impiegatoSrv.checkExistanceOfImpiegatoByEmail(newEmail)){
//			return this.buildUnprocessableEntity("Email owned by onother user");
//		}
//		Impiegato impiegato = request.getFkIdUsersAuthDetails();
//		impiegato.setEmail(newEmail);
//		
//		this.impiegatoRepo.save(impiegato);
//		this.changeEmailRepo.delete(request);
//		
//		return this.buildOkResponse(new StringDTO("Email changed successfully"));
//	}
	
	
}
