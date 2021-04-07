package it.palex.attendanceManagement.core.controllers.rest.endpoints.user;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.palex.attendanceManagement.core.dtos.personalDocuments.PaycheckAddDTO;
import it.palex.attendanceManagement.core.service.personalDocument.PaycheckWebService;
import it.palex.attendanceManagement.data.dto.documents.TicketDownloadDTO;
import it.palex.attendanceManagement.data.dto.impiegato.PaycheckDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 7 giu 2020
 */
@RestController
@RequestMapping(path="paycheck")
public class PaycheckUserController extends RestEndpoint {

	@Autowired
	private PaycheckWebService paycheckWebService;
	
	
	@GetMapping(path = "/findAllMyPaychecks")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<PaycheckDTO>>> findAllMyPaychecks(
			@RequestParam(value="year", required= false) Integer year,
			@RequestParam(value="month", required= false) Integer month,
			@PageableDefault(page = 0, size = 10, sort={"year", "month"},direction=Direction.DESC) Pageable pageable) {
		
		GenericResponse<Page<PaycheckDTO>> res = this.paycheckWebService
				.findAllPaycheckOfCurrentLoggedUser(year, month, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path = "/findAllPaychecksOfUser")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PAYCHECK_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<PaycheckDTO>>> findAllPaychecksOfUser(
			@RequestParam(value="userProfileId", required= true) Integer userProfileId,
			@RequestParam(value="year", required= false) Integer year,
			@RequestParam(value="month", required= false) Integer month,
			@PageableDefault(page = 0, size = 10,sort={"year", "month"},direction=Direction.DESC) Pageable pageable) {
		
		GenericResponse<Page<PaycheckDTO>> res = this.paycheckWebService
				.findAllPaycheckOfUser(userProfileId, year, month, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@PostMapping(path = "/uploadPayckeck")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PAYCHECK_CREATE+"')")
	@Loggable(logExecutionTime = true, logParameters = true, logResponseParameter = true)
	public ResponseEntity<GenericResponse<PaycheckDTO>> uploadPayckeck(
			 @RequestPart(value = "paycheck", required = true) MultipartFile paycheck,
			 @RequestPart(value = "info", required = true) String info) throws IOException, Exception {
	
		PaycheckAddDTO toMap = this.jsonStringToObject(info, PaycheckAddDTO.class);
		
		GenericResponse<PaycheckDTO> res = this.paycheckWebService.addNewPaycheck(toMap, paycheck);
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path = "/downloadMyPaycheck")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TicketDownloadDTO>> downloadMyPaycheck(
			@RequestParam(value="paycheckId", required= true) Long paycheckId) {
		
		GenericResponse<TicketDownloadDTO> res = this.paycheckWebService
				.downloadMyPaycheck(paycheckId);
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path = "/downloadUserPaycheck")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PAYCHECK_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TicketDownloadDTO>> downloadUserPaycheck(
			@RequestParam(value="paycheckId", required= true) Long paycheckId) {
		
		GenericResponse<TicketDownloadDTO> res = this.paycheckWebService
				.downloadUserPaycheck(paycheckId);
		
		return this.buildGenericResponse(res);
	}
	
	@DeleteMapping(path = "/deleteUserPaycheck")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.PAYCHECK_DELETE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> deleteUserPaycheck(
			@RequestParam(value="paycheckId", required= true) Long paycheckId) throws Exception {
		
		GenericResponse<StringDTO> res = this.paycheckWebService
				.deleteUserPaycheck(paycheckId);
		
		return this.buildGenericResponse(res);
	}
	
}
