package it.palex.attendanceManagement.core.controllers.rest.endpoints.user;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.palex.attendanceManagement.core.dtos.userProfile.ExpenseReportCreationRequest;
import it.palex.attendanceManagement.core.dtos.userProfile.ExpenseReportDetailsDTO;
import it.palex.attendanceManagement.core.dtos.userProfile.ExpenseReportUpdateRequest;
import it.palex.attendanceManagement.core.dtos.userProfile.ExpenseReportUpdateRequestByExpenseManager;
import it.palex.attendanceManagement.core.dtos.userProfile.ReportElementAddRequest;
import it.palex.attendanceManagement.core.service.userProfile.ExpenseReportWebService;
import it.palex.attendanceManagement.data.dto.core.ExpenseReportDTO;
import it.palex.attendanceManagement.data.dto.core.ExpenseReportElementDTO;
import it.palex.attendanceManagement.data.dto.documents.TicketDownloadDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 8 lug 2020
 */
@RestController
@RequestMapping(path="expenseReport")
public class ExpenseReportController extends RestEndpoint {

	@Autowired
	private ExpenseReportWebService expenseReportWebService;
	
	
	@PostMapping(path="update")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<ExpenseReportDTO>> update(
			@RequestBody ExpenseReportUpdateRequest updateReq) {
		
		GenericResponse<ExpenseReportDTO> res = this.expenseReportWebService
				.updateReport(updateReq);
		
		return this.buildGenericResponse(res);
	}
	
	@PostMapping(path="create")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<ExpenseReportDTO>> create(
			@RequestBody ExpenseReportCreationRequest addReq) {
		
		GenericResponse<ExpenseReportDTO> res = this.expenseReportWebService
				.createNewReport(addReq);
		
		return this.buildGenericResponse(res);
	}
	
	@PostMapping(path = "/addNewReportElementToMyReport")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = true, logResponseParameter = true)
	public ResponseEntity<GenericResponse<ExpenseReportElementDTO>> addNewReportElementToMyReport(
			 @RequestPart(value = "attachment", required = true) MultipartFile attachment,
			 @RequestPart(value = "info", required = true) String info) throws IOException, Exception {
	
		ReportElementAddRequest element = this.jsonStringToObject(info, ReportElementAddRequest.class);
		
		GenericResponse<ExpenseReportElementDTO> res = this.expenseReportWebService.addNewReportElementToMyReport(element, attachment);
		
		return this.buildGenericResponse(res);
	}
	
	
	
	@GetMapping(path="downloadExpenseOfReportAttachment")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TicketDownloadDTO>> downloadExpenseOfReportAttachment(
			@RequestParam(name = "reportElementId", required = true) Long reportElementId){
		GenericResponse<TicketDownloadDTO> res = this.expenseReportWebService
				.downloadExpenseOfReportAttachment(reportElementId);
		
		return this.buildGenericResponse(res);
	}
	
	@DeleteMapping(path="deleteReportElement")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> deleteReportElement(
			@RequestParam(name = "reportElementId", required = true) Long reportElementId) throws Exception{
		GenericResponse<StringDTO> res = this.expenseReportWebService
				.deleteExpenseOfReport(reportElementId);
		
		return this.buildGenericResponse(res);
	}
	
	@DeleteMapping(path="deleteReport")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> deleteReport(
			@RequestParam(name = "reportId", required = true) Long reportId) throws Exception{
		GenericResponse<StringDTO> res = this.expenseReportWebService
				.deleteReport(reportId);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path="findMyExpenseReportDetails")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<ExpenseReportDetailsDTO>> findMyExpenseReportDetails(
			@RequestParam(name = "expenseId", required = true) Long expenseId){
		GenericResponse<ExpenseReportDetailsDTO> res = this.expenseReportWebService
				.findMyExpenseReportDetails(expenseId);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path="findAllMyExpenseReport")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<ExpenseReportDTO>>> findAllMyExpenseReport(
			@RequestParam(name = "dateOfExpenceFrom", required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateOfExpenceFrom,
			@RequestParam(name = "dateOfExpenceTo", required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateOfExpenceTo,
			
			@RequestParam(name = "status", required = false) String status,
			@RequestParam(name = "location", required = false) String location,
			@RequestParam(name = "title", required = false) String title,
			@PageableDefault(page = 0, size = 10, sort={"dateOfExpence"}, direction=Direction.DESC) Pageable pageable
			) {
		
		Date dateFrom = null;
		if(dateOfExpenceFrom!=null) {
			ZonedDateTime zonedDateTime = dateOfExpenceFrom.atZone(ZoneId.systemDefault());
			dateFrom = Date.from(zonedDateTime.toInstant());
		}
		Date dateTo = null;
		if(dateOfExpenceTo!=null) {
			ZonedDateTime zonedDateTime = dateOfExpenceTo.atZone(ZoneId.systemDefault());
			dateTo = Date.from(zonedDateTime.toInstant());
		}
		
		GenericResponse<Page<ExpenseReportDTO>> res = this.expenseReportWebService
				.findAllExpenceReportOfCurrentLoggedUser(status, location, title, dateFrom, dateTo, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path="findUserExpenseReportDetails")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_EXPENSE_REPORT+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<ExpenseReportDetailsDTO>> findUserExpenseReportDetails(
			@RequestParam(name = "reportElementId", required = true) Long reportElementId){
		GenericResponse<ExpenseReportDetailsDTO> res = this.expenseReportWebService
				.findUserExpenseReportDetails(reportElementId);
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path="downloadExpenseOfUserReportAttachment")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_EXPENSE_REPORT+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TicketDownloadDTO>> downloadExpenseOfUserReportAttachment(
			@RequestParam(name = "reportElementId", required = true) Long reportElementId){
		GenericResponse<TicketDownloadDTO> res = this.expenseReportWebService
				.downloadExpenseOfUserReportAttachment(reportElementId);
		
		return this.buildGenericResponse(res);
	}
	
	
	@PostMapping(path="refuseExpenseReportElement")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_EXPENSE_REPORT+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<ExpenseReportElementDTO>> refuseExpenseReportElement(
			@RequestParam(name = "reportElementId", required = true) Long reportElementId){
		GenericResponse<ExpenseReportElementDTO> res = this.expenseReportWebService
				.refuseExpenseReportElement(reportElementId);
		
		return this.buildGenericResponse(res);
	}
	
	@PostMapping(path="updateExpenseReport")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_EXPENSE_REPORT+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<ExpenseReportDTO>> updateExpenseReport(
			@RequestBody ExpenseReportUpdateRequestByExpenseManager updateReq){
		GenericResponse<ExpenseReportDTO> res = this.expenseReportWebService
				.updateExpenseReport(updateReq);
		
		return this.buildGenericResponse(res);
	}
	
	@PostMapping(path="acceptExpenseReportElement")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_EXPENSE_REPORT+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<ExpenseReportElementDTO>> acceptExpenseReportElement(
			@RequestParam(name = "reportElementId", required = true) Long reportElementId){
		
		GenericResponse<ExpenseReportElementDTO> res = this.expenseReportWebService
				.acceptExpenseReportElement(reportElementId);
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path="findAllExpenseReportOfUsers")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_EXPENSE_REPORT+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<ExpenseReportDTO>>> findAllExpenseReportOfUsers(
			@RequestParam(name = "dateOfExpenceFrom", required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateOfExpenceFrom,
			@RequestParam(name = "dateOfExpenceTo", required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateOfExpenceTo,
			
			@RequestParam(name = "madeByEmail", required = false) String madeByEmail,
			@RequestParam(name = "madeByName", required = false) String madeByName,
			@RequestParam(name = "madeBySurname", required = false) String madeBySurname,
			@RequestParam(name = "status", required = false) String status,
			@RequestParam(name = "location", required = false) String location,
			@RequestParam(name = "title", required = false) String title,
			@PageableDefault(page = 0, size = 10, sort={"dateOfExpence"}, direction=Direction.DESC) Pageable pageable
			) {
		Date dateFrom = null;
		if(dateOfExpenceFrom!=null) {
			ZonedDateTime zonedDateTime = dateOfExpenceFrom.atZone(ZoneId.systemDefault());
			dateFrom = Date.from(zonedDateTime.toInstant());
		}
		Date dateTo = null;
		if(dateOfExpenceFrom!=null) {
			ZonedDateTime zonedDateTime = dateOfExpenceTo.atZone(ZoneId.systemDefault());
			dateTo = Date.from(zonedDateTime.toInstant());
		}
		
		GenericResponse<Page<ExpenseReportDTO>> res = this.expenseReportWebService
				.findAllExpenseReportOfUsers(madeByEmail, madeByName, madeBySurname, status, 
						location, title, dateFrom, dateTo, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
}
