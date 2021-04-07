package it.palex.attendanceManagement.core.service.personalDocument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.palex.attendanceManagement.commons.utils.PdfUtils;
import it.palex.attendanceManagement.core.dtos.personalDocuments.PaycheckAddDTO;
import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.dto.documents.TicketDownloadDTO;
import it.palex.attendanceManagement.data.dto.impiegato.PaycheckDTO;
import it.palex.attendanceManagement.data.dto.transformers.PaycheckTransformer;
import it.palex.attendanceManagement.data.dto.transformers.TicketDownloadTransformer;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.TicketDownload;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.Paycheck;
import it.palex.attendanceManagement.data.entities.enumTypes.StandardDocumentDescription;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.core.PaycheckService;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.data.service.documento.TicketDownloadService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.FileUtility;
import it.palex.attendanceManagement.library.utils.HttpCodes;

@Service
public class PaycheckWebService implements GenericService {
	
//	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PaycheckWebService.class);
		
	public static final String PAYCHECK_SUPPORTED_EXTENSION = "pdf";
	@Autowired
	private PaycheckService paycheckService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	@Autowired
	private TicketDownloadService ticketDownloadService;
	
	
	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.INTERNAL_SERVER_ERROR, HttpCodes.BAD_REQUEST})
	public GenericResponse<PaycheckDTO> addNewPaycheck(PaycheckAddDTO request, MultipartFile paycheck) throws IOException, Exception{
		if(request==null || request.getUserProfileId()==null || request.getMonth()==null || 
				request.getYear()==null || paycheck==null) {
			return this.buildBadDataResponse();
		}
				
		UserProfile profile = this.userProfileService.findById(request.getUserProfileId());
		
		if(profile==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.USER_NOT_FOUND);
		}
		
				
		//if not forced to add paycheck check if already exists
		if(!BooleanUtils.isTrue(request.getForceAdd())) { 
			boolean existsPaycheckAlready = this.paycheckService.checkExistanceOfAnotherPaycheckInMonthAndDateForUser(
					profile, request.getYear(), request.getMonth());
			
			if(existsPaycheckAlready) {
				return this.buildConflictEntity(StandardReturnCodesEnum.ALREADY_EXISTS_A_PAYOUT_IN_MONTH_AND_DATE);
			}			
		}	
		
		String ext = FileUtility.getFileExtension(paycheck.getOriginalFilename()); 
		
		if(ext==null) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.UNSUPPORTED_FILE_EXTENSIONS);
		}
		ext = StringUtils.lowerCase(ext.toLowerCase());
		
		//extensions are saved in database with initial dot
		if(!StringUtils.equalsIgnoreCase(ext, PAYCHECK_SUPPORTED_EXTENSION)) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.UNSUPPORTED_FILE_EXTENSIONS);
		}
		
		InputStream fileInputStream = paycheck.getInputStream();
		
		//validations
		if(BooleanUtils.isTrue(request.getCheckFiscalCode())){
			//if user has no fiscal code
			if(profile.getFiscalCode()==null) {
				return this.buildUnprocessableEntity(StandardReturnCodesEnum.FISCAL_CODE_OF_USER_IS_NOT_FOUND_IN_PAYCHECK);
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(fileInputStream, baos);
			baos.close();
			fileInputStream.close();
			
			InputStream fileStreamClone = new ByteArrayInputStream(baos.toByteArray()); 
			
			boolean isPresent = isPresentStringInPaycheck(profile.getFiscalCode(), fileStreamClone);
			
			if(!isPresent) {
				return this.buildUnprocessableEntity(StandardReturnCodesEnum.FISCAL_CODE_OF_USER_IS_NOT_FOUND_IN_PAYCHECK);	
			}
			fileStreamClone = null; //make collactable by gc
			
			fileInputStream = new ByteArrayInputStream(baos.toByteArray()); 
		}
		
		Document paycheckDoc = this.documentService.saveFileWithDefaultCryptedFM(paycheck.getOriginalFilename(), 
				fileInputStream, profile.getId(), StandardDocumentDescription.PAYCHECK.name());
		
		Paycheck paycheckCreated = new Paycheck();
		paycheckCreated.setDocument(paycheckDoc);
		paycheckCreated.setMonth(request.getMonth());
		paycheckCreated.setSendEmailDate(null);
		paycheckCreated.setTitle(request.getTitle());
		paycheckCreated.setUserProfile(profile);
		paycheckCreated.setYear(request.getYear());
		
		if(!paycheckCreated.canBeInsertedInDatabase()) {
			//clear file created
			this.documentService.deleteOnlyFile(paycheckDoc);
			
			return this.buildBadDataResponse();
		}
		
		paycheckCreated = this.paycheckService.saveOrUpdate(paycheckCreated);
		PaycheckDTO dto = PaycheckTransformer.mapToDTO(paycheckCreated);
		
		return this.buildOkResponse(dto);
	}
	
	private boolean isPresentStringInPaycheck(String fiscalCode, InputStream file) throws IOException {
		boolean isPresent = PdfUtils.checExistanceOfStringOccurrencies(fiscalCode, file, true);
		
		return isPresent;
	}
	
	public GenericResponse<Page<PaycheckDTO>> findAllPaycheckOfUser(
			Integer userProfileId, Integer year, Integer month, Pageable pageable){
		if(userProfileId==null || pageable==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.userProfileService.findById(userProfileId);
		
		if(profile==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.USER_NOT_FOUND);
		}
		
		return findPagedPaycheckOfUser(year, month, pageable, profile);
	}
	
	
	public GenericResponse<Page<PaycheckDTO>> findAllPaycheckOfCurrentLoggedUser(
			Integer year, Integer month, Pageable pageable){
		if( pageable==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		return findPagedPaycheckOfUser(year, month, pageable, profile);
	}

	private GenericResponse<Page<PaycheckDTO>> findPagedPaycheckOfUser(Integer year, Integer month, Pageable pageable,
			UserProfile profile) {
		List<Paycheck> paychecks = this.paycheckService.findAllPaycheckOfUser(profile, year, month, pageable);
		
		List<PaycheckDTO> list = PaycheckTransformer.mapToDTO(paychecks);
		long totalCount = this.paycheckService.countAllPaycheckOfUser(profile, year, month);
		
		
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}


	public GenericResponse<TicketDownloadDTO> downloadMyPaycheck(Long paycheckId) {
		if(paycheckId==null) {
			return this.buildBadDataResponse();
		}
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		//use this function to avoid that a user try to download another user paycheck knowing the id
		Paycheck paycheck = this.paycheckService.findByIdAndUserProfile(paycheckId, profile);
		
		if(paycheck==null) {
			return this.buildNotFoundResponse();
		}
		
		TicketDownload ticket= this.ticketDownloadService.createAndSaveOneShotTicketForDocument(paycheck.getDocument());

		return this.buildOkResponse(TicketDownloadTransformer.mapToDTO(ticket));
	}


	public GenericResponse<TicketDownloadDTO> downloadUserPaycheck(Long paycheckId) {
		if(paycheckId==null) {
			return this.buildBadDataResponse();
		}
		
		Paycheck paycheck = this.paycheckService.findById(paycheckId);
		
		if(paycheck==null) {
			return this.buildNotFoundResponse();
		}
		
		TicketDownload ticket= this.ticketDownloadService.createAndSaveOneShotTicketForDocument(paycheck.getDocument());
		
		return this.buildOkResponse(TicketDownloadTransformer.mapToDTO(ticket));
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> deleteUserPaycheck(Long paycheckId) throws Exception {
		if(paycheckId==null) {
			return this.buildBadDataResponse();
		}
		
		Paycheck paycheck = this.paycheckService.findById(paycheckId);
		
		if(paycheck==null) {
			return this.buildNotFoundResponse();
		}
		
		this.paycheckService.delete(paycheck);
		
		this.documentService.deleteDocumentAndFile(paycheck.getDocument());
		
		return this.buildStringOkResponse("Paycheck successfully deleted");
	}
	
	
}
