package it.palex.attendanceManagement.core.service.personalDocument;

import java.io.IOException; 
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.palex.attendanceManagement.core.dtos.personalDocuments.PersonalDocumentAddRequest;
import it.palex.attendanceManagement.data.dto.core.PersonalDocumentDTO;
import it.palex.attendanceManagement.data.dto.core.PersonalDocumentTypeDTO;
import it.palex.attendanceManagement.data.dto.documents.TicketDownloadDTO;
import it.palex.attendanceManagement.data.dto.transformers.TicketDownloadTransformer;
import it.palex.attendanceManagement.data.dto.transformers.core.PersonalDocumentTransformer;
import it.palex.attendanceManagement.data.dto.transformers.core.PersonalDocumentTypeTransformer;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.TicketDownload;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.PersonalDocument;
import it.palex.attendanceManagement.data.entities.core.PersonalDocumentType;
import it.palex.attendanceManagement.data.entities.enumTypes.StandardDocumentDescription;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.core.PersonalDocumentService;
import it.palex.attendanceManagement.data.service.core.PersonalDocumentTypeService;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.data.service.documento.TicketDownloadService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.FileUtility;

@Service
public class PersonalDocumentWebService implements GenericService {

	@Autowired
	private PersonalDocumentService personalDocumentService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	@Autowired
	private PersonalDocumentTypeService personalDocumentTypeService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private TicketDownloadService ticketDownloadService;
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<PersonalDocumentDTO> uploadNewPersonalDocumentForUser(MultipartFile file, 
			PersonalDocumentAddRequest toAdd) throws IOException, Exception{
		if(toAdd==null || toAdd.getUserProfileId()==null) {
			return this.buildBadDataResponse();
		}
		UserProfile user = this.userProfileService.findById(toAdd.getUserProfileId());
		
		if(user==null) {
			return this.buildNotFoundResponse();
		}
		
		return this.uploadNewPersonalDocumentForUser(user, file, toAdd.getDocumentTypeId(), BooleanUtils.toBoolean(toAdd.getEditable()));
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<PersonalDocumentDTO> uploadNewPersonalDocumentForCurrentLoggedUser(MultipartFile file, 
			PersonalDocumentAddRequest toAdd) throws IOException, Exception{
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		return this.uploadNewPersonalDocumentForUser(user, file, toAdd.getDocumentTypeId(), true);
	}
	
	private GenericResponse<PersonalDocumentDTO> uploadNewPersonalDocumentForUser(UserProfile user, MultipartFile file, 
			Integer documentTypeId, boolean editable) throws IOException, Exception{
		if(file==null || documentTypeId==null) {
			return this.buildBadDataResponse();
		}
		
		PersonalDocumentType type = this.personalDocumentTypeService.findById(documentTypeId);
		
		if(type==null) {
			return this.buildNotFoundResponse("Personal document Type not found");
		}
		
		List<String> supportedExtensionsLowecase = this.personalDocumentTypeService.buildSupportedExtensionsLowercase(type);
		
		String ext = FileUtility.getFileExtension(file.getOriginalFilename()); 
		
		if(ext==null) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.UNSUPPORTED_FILE_EXTENSIONS);
		}
		
		//extensions are saved in database with initial dot
		if(!ext.startsWith(".")) {
			ext = "."+ext;
		}
		ext = ext.toLowerCase();
		
		if(!supportedExtensionsLowecase.contains(ext)) {
			return this.buildBadDataResponse(StandardReturnCodesEnum.UNSUPPORTED_FILE_EXTENSIONS);
		}
		
		
		boolean existsDocument = this.personalDocumentService.existDocumentTypeForUser(user, type.getId());
		
		if(existsDocument) {
			return this.buildConflictEntity(StandardReturnCodesEnum.ALREADY_EXIST_THIS_PERSONAL_DOCUMENT_FOR_USER);
		}
		
		Document doc = this.documentService.saveFileWithDefaultCryptedFM(file.getOriginalFilename(), 
				file.getInputStream(), user.getId(), StandardDocumentDescription.PERSONAL_DOCUMENT.name());
		
		PersonalDocument newDoc = new PersonalDocument();
		newDoc.setPersonalDocumentType(type);
		newDoc.setUploadDate(DateUtility.getCurrentDateInUTC());
		newDoc.setUserProfile(user);
		newDoc.setDocument(doc);
		newDoc.setEditable(editable);

		newDoc = this.personalDocumentService.saveOrUpdate(newDoc);
		
		return this.buildOkResponse(PersonalDocumentTransformer.mapToDTO(newDoc));
	}
	
	
	public GenericResponse<Page<PersonalDocumentDTO>> findAllPersonalDocumentOfUser(Integer userProfileId, 
			String documentType, Pageable pageable){
		if(userProfileId==null) {
			return this.buildBadDataResponse();
		}
		UserProfile user = this.userProfileService.findById(userProfileId);
		
		if(user==null) {
			return this.buildNotFoundResponse();
		}
		
		return this.findAllPersonalDocumentOfUserPrivate(user, documentType, pageable);
	}
	
	public GenericResponse<Page<PersonalDocumentDTO>> findAllPersonalDocumentOfCurrentLoggedUser(
			String documentType, Pageable pageable){
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		return this.findAllPersonalDocumentOfUserPrivate(user, documentType, pageable);
	}
	
	private GenericResponse<Page<PersonalDocumentDTO>> findAllPersonalDocumentOfUserPrivate(
			UserProfile user, String documentType, Pageable pageable){
		if(pageable==null) {
			return this.buildBadDataResponse();
		}
		
		List<PersonalDocument> list = this.personalDocumentService.findDocumentsOfUser(user, documentType, pageable);
		long totalCount = this.personalDocumentService.countDocumentsOfUser(user, documentType);
		
		List<PersonalDocumentDTO> res = PersonalDocumentTransformer.mapToDTO(list);
		
		return this.buildPageableOkResponse(res, totalCount, pageable);
	}
	
	
	
	
	
	public GenericResponse<List<PersonalDocumentTypeDTO>> findAllNotUploadedDocumentTypeOfUser(Integer userProfileId){
		if(userProfileId==null) {
			return this.buildBadDataResponse();
		}
		UserProfile user = this.userProfileService.findById(userProfileId);
		
		return this.findAllNotUploadedDocumentTypeOfUser(user);
	}
	
	public GenericResponse<List<PersonalDocumentTypeDTO>> findAllMyNotUploadedDocumentType(){
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		return this.findAllNotUploadedDocumentTypeOfUser(user);
	}
	
	private GenericResponse<List<PersonalDocumentTypeDTO>> findAllNotUploadedDocumentTypeOfUser(UserProfile user){
		List<PersonalDocumentType> personalDocumentType = this.personalDocumentService.findAllDocumentNotYetUploadedByUser(user);
		List<PersonalDocumentTypeDTO> res = PersonalDocumentTypeTransformer.mapToDTO(personalDocumentType);
		
		return this.buildOkResponse(res);
	}
	
	public GenericResponse<List<PersonalDocumentTypeDTO>> findAllPersonalDocumentTypeDocuments(){
		List<PersonalDocumentType> personalDocumentType = this.personalDocumentTypeService.findAll();
		List<PersonalDocumentTypeDTO> res = PersonalDocumentTypeTransformer.mapToDTO(personalDocumentType);
		
		return this.buildOkResponse(res);
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> deletePersonalDocumentOfUser(Long personalDocumentId) throws Exception{
		if(personalDocumentId==null) {
			return this.buildBadDataResponse();
		}
		PersonalDocument document = this.personalDocumentService.findById(personalDocumentId);
		
		if(document==null) {
			return this.buildNotFoundResponse();
		}
		
		this.personalDocumentService.delete(document);
		this.documentService.deleteDocumentAndFile(document.getDocument());
		
		return this.buildStringOkResponse("Successfully deleted");
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> deletePersonalDocumentOfCurrentLoggedUser(Long personalDocumentId) throws Exception{
		if(personalDocumentId==null) {
			return this.buildBadDataResponse();
		}
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		//Use the id only if you use the chain permission evaluator
		//Use this method to check that the document belongs to user. DO NOT CHECK JUST THE ID
		PersonalDocument document = this.personalDocumentService.findByIdAndUserProfile(personalDocumentId, user);
	
		if(document==null) {
			return this.buildNotFoundResponse();
		}
		
		if(BooleanUtils.isFalse(document.getEditable())) {
			return this.buildNotAcceptableResponse(StandardReturnCodesEnum.PERSONAL_DOCUMENT_IN_NOT_EDITABLE);
		}
		
		this.personalDocumentService.delete(document);
		this.documentService.deleteDocumentAndFile(document.getDocument());
		
		return this.buildStringOkResponse("Successfully deleted");
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<PersonalDocumentDTO> disableEdit(Long personalDocumentId) {
		return this.enableDisableEdit(personalDocumentId, false);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<PersonalDocumentDTO> enableEdit(Long personalDocumentId) {
		return this.enableDisableEdit(personalDocumentId, true);
	}
	
	private GenericResponse<PersonalDocumentDTO> enableDisableEdit(Long personalDocumentId, boolean editable){
		if(personalDocumentId==null) {
			return this.buildBadDataResponse();
		}
		
		PersonalDocument personalDocument = this.personalDocumentService.findById(personalDocumentId);
		
		if(personalDocument==null) {
			return this.buildNotFoundResponse();
		}
		personalDocument.setEditable(editable);
		
		personalDocument = this.personalDocumentService.saveOrUpdate(personalDocument);
		
		return this.buildOkResponse(PersonalDocumentTransformer.mapToDTO(personalDocument));
	}
	
	
	
	public GenericResponse<TicketDownloadDTO> downloadPersonalDocumentOfUser(Long personalDocumentId) {
		if(personalDocumentId==null) {
			return this.buildBadDataResponse();
		}
		
		PersonalDocument personalDocument = this.personalDocumentService.findById(personalDocumentId);
		
		if(personalDocument==null) {
			return this.buildNotFoundResponse();
		}
		
		TicketDownload ticket= this.ticketDownloadService.createAndSaveOneShotTicketForDocument(personalDocument.getDocument());
		
		return this.buildOkResponse(TicketDownloadTransformer.mapToDTO(ticket));
	} 
	
	public GenericResponse<TicketDownloadDTO> downloadPersonalDocumentOfCurrentLoggedUser(Long personalDocumentId) {
		if(personalDocumentId==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		//Use the id only if you use the chain permission evaluator
		//Use this method to check that the document belongs to user. DO NOT CHECK JUST THE ID
		PersonalDocument personalDocument = this.personalDocumentService.findByIdAndUserProfile(personalDocumentId, user);
	
		if(personalDocument==null) {
			return this.buildNotFoundResponse();
		}
		
		TicketDownload ticket= this.ticketDownloadService.createAndSaveOneShotTicketForDocument(personalDocument.getDocument());
		
		return this.buildOkResponse(TicketDownloadTransformer.mapToDTO(ticket));
	}

	
}
