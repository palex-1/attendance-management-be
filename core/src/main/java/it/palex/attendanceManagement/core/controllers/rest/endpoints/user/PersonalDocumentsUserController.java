package it.palex.attendanceManagement.core.controllers.rest.endpoints.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.palex.attendanceManagement.core.dtos.personalDocuments.PersonalDocumentAddRequest;
import it.palex.attendanceManagement.core.service.personalDocument.PersonalDocumentWebService;
import it.palex.attendanceManagement.data.dto.core.PersonalDocumentDTO;
import it.palex.attendanceManagement.data.dto.core.PersonalDocumentTypeDTO;
import it.palex.attendanceManagement.data.dto.documents.TicketDownloadDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping(path="personalDocuments")
public class PersonalDocumentsUserController extends RestEndpoint {

	@Autowired
	private PersonalDocumentWebService personalDocumentWebService;
	
	
	@GetMapping(path = "/findAllMyDocuments")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<PersonalDocumentDTO>>> findAllMyDocuments(
			@RequestParam(value="documentType", required= false) String documentType,
			@PageableDefault(page = 0, size = 10, sort={"personalDocumentType.type"},direction=Direction.DESC) Pageable pageable) {
		GenericResponse<Page<PersonalDocumentDTO>> res = this.personalDocumentWebService
				.findAllPersonalDocumentOfCurrentLoggedUser(documentType, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path = "/findAllDocumentsOfUser")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PERSONAL_DOCUMENT_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<PersonalDocumentDTO>>> findAllDocumentsOfUser(
			@RequestParam(value="userProfileId", required= true) Integer userProfileId,
			@RequestParam(value="documentType", required= false) String documentType,
			@PageableDefault(page = 0, size = 10, sort={"personalDocumentType.type"},direction=Direction.DESC) Pageable pageable) {
		
		GenericResponse<Page<PersonalDocumentDTO>> res = this.personalDocumentWebService
				.findAllPersonalDocumentOfUser(userProfileId, documentType, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path = "/findAllDocumentType")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<PersonalDocumentTypeDTO>>> findAllDocumentType() {
		GenericResponse<List<PersonalDocumentTypeDTO>> res = this.personalDocumentWebService
				.findAllPersonalDocumentTypeDocuments();
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path = "/findAllMyNotUploadedDocumentType")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<PersonalDocumentTypeDTO>>> findAllMyNotUploadedDocumentType() {
		GenericResponse<List<PersonalDocumentTypeDTO>> res = this.personalDocumentWebService
				.findAllMyNotUploadedDocumentType();
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path = "/findAllNotUploadedDocumentTypeOfUser")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PERSONAL_DOCUMENT_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<PersonalDocumentTypeDTO>>> findAllNotUploadedDocumentTypeOfUser(
			@RequestParam(value="userProfileId", required= true) Integer userProfileId) {
		
		GenericResponse<List<PersonalDocumentTypeDTO>> res = this.personalDocumentWebService
				.findAllNotUploadedDocumentTypeOfUser(userProfileId);
		
		return this.buildGenericResponse(res);
	}
		
	
	
	@PutMapping(path = "/enableEdit")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PERSONAL_DOCUMENT_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = true, logResponseParameter = true)
	public ResponseEntity<GenericResponse<PersonalDocumentDTO>> enableEdit(
			@RequestParam(value="personalDocumentId", required= true) Long personalDocumentId) throws IOException, Exception {
			
		GenericResponse<PersonalDocumentDTO> res = this.personalDocumentWebService
				.enableEdit(personalDocumentId);
		
		return this.buildGenericResponse(res);
	}
	
	@PutMapping(path = "/disableEdit")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PERSONAL_DOCUMENT_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = true, logResponseParameter = true)
	public ResponseEntity<GenericResponse<PersonalDocumentDTO>> disableEdit(
			@RequestParam(value="personalDocumentId", required= true) Long personalDocumentId) throws IOException, Exception {
			
		GenericResponse<PersonalDocumentDTO> res = this.personalDocumentWebService
				.disableEdit(personalDocumentId);
		
		return this.buildGenericResponse(res);
	}
		
	@PostMapping(path = "/uploadMyDocument")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = true, logResponseParameter = true)
	public ResponseEntity<GenericResponse<PersonalDocumentDTO>> uploadMyDocument(
			 @RequestPart(value = "document", required = true) MultipartFile file,
			 @RequestPart(value = "info", required = true) String info) throws IOException, Exception {
	
		PersonalDocumentAddRequest toAdd = this.jsonStringToObject(info, PersonalDocumentAddRequest.class);
		
		GenericResponse<PersonalDocumentDTO> res = this.personalDocumentWebService
				.uploadNewPersonalDocumentForCurrentLoggedUser(file, toAdd);
		
		return this.buildGenericResponse(res);
	}
	
	@PostMapping(path = "/uploadUserDocument")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PERSONAL_DOCUMENT_CREATE+"')")
	@Loggable(logExecutionTime = true, logParameters = true, logResponseParameter = true)
	public ResponseEntity<GenericResponse<PersonalDocumentDTO>> uploadUserDocument(
			 @RequestPart(value = "document", required = true) MultipartFile file,
			 @RequestPart(value = "info", required = true) String info) throws IOException, Exception {
	
		PersonalDocumentAddRequest toAdd = this.jsonStringToObject(info, PersonalDocumentAddRequest.class);
		
		GenericResponse<PersonalDocumentDTO> res = this.personalDocumentWebService
				.uploadNewPersonalDocumentForUser(file, toAdd);
		
		return this.buildGenericResponse(res);
	}
	
	
	
	
	
	@GetMapping(path = "/downloadMyDocument")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TicketDownloadDTO>> downloadMyDocument(
			@RequestParam(value="personalDocumentId", required= true) Long personalDocumentId) {
		
		GenericResponse<TicketDownloadDTO> res = this.personalDocumentWebService
				.downloadPersonalDocumentOfCurrentLoggedUser(personalDocumentId);
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path = "/downloadUserDocument")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PERSONAL_DOCUMENT_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TicketDownloadDTO>> downloadUserDocument(
			@RequestParam(value="personalDocumentId", required= true) Long personalDocumentId) {
		
		GenericResponse<TicketDownloadDTO> res = this.personalDocumentWebService
				.downloadPersonalDocumentOfUser(personalDocumentId);
		
		return this.buildGenericResponse(res);
	}
	
	
	@DeleteMapping(path = "/deleteMyDocument")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> deleteMyDocument(
			@RequestParam(value="personalDocumentId", required= true) Long personalDocumentId) throws Exception {
		
		GenericResponse<StringDTO> res = this.personalDocumentWebService
						.deletePersonalDocumentOfCurrentLoggedUser(personalDocumentId);
		
		return this.buildGenericResponse(res);
	}
	
	@DeleteMapping(path = "/deleteUserDocument")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PERSONAL_DOCUMENT_DELETE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> deleteUserDocument(
			@RequestParam(value="personalDocumentId", required= true) Long personalDocumentId) throws Exception {
		
		GenericResponse<StringDTO> res = this.personalDocumentWebService
						.deletePersonalDocumentOfUser(personalDocumentId);
		
		return this.buildGenericResponse(res);
	}
}
