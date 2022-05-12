package it.palex.attendanceManagement.core.service.userProfile;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.palex.attendanceManagement.core.dtos.userProfile.ExpenseReportCreationRequest;
import it.palex.attendanceManagement.core.dtos.userProfile.ExpenseReportDetailsDTO;
import it.palex.attendanceManagement.core.dtos.userProfile.ExpenseReportUpdateRequest;
import it.palex.attendanceManagement.core.dtos.userProfile.ExpenseReportUpdateRequestByExpenseManager;
import it.palex.attendanceManagement.core.dtos.userProfile.ReportElementAddRequest;
import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.dto.core.ExpenseReportDTO;
import it.palex.attendanceManagement.data.dto.core.ExpenseReportElementDTO;
import it.palex.attendanceManagement.data.dto.documents.TicketDownloadDTO;
import it.palex.attendanceManagement.data.dto.transformers.ExpenseReportTransformer;
import it.palex.attendanceManagement.data.dto.transformers.TicketDownloadTransformer;
import it.palex.attendanceManagement.data.dto.transformers.core.ExpenseReportElementTransformer;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.TicketDownload;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.core.ExpenseReport;
import it.palex.attendanceManagement.data.entities.core.ExpenseReportElement;
import it.palex.attendanceManagement.data.entities.enumTypes.ExpenseReportStatusEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.StandardDocumentDescription;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.core.ExpenseReportElementService;
import it.palex.attendanceManagement.data.service.core.ExpenseReportService;
import it.palex.attendanceManagement.data.service.core.TaskCompletionsLocksService;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.data.service.documento.TicketDownloadService;
import it.palex.attendanceManagement.data.service.incarico.WorkTaskService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.HttpCodes;
import it.palex.attendanceManagement.library.utils.StringUtility;

@Service
public class ExpenseReportWebService implements GenericService {

	@Autowired
	private ExpenseReportService expenseReportService;
	
	@Autowired
	private WorkTaskService workTaskService;
	
	@Autowired
	private ExpenseReportElementService expenseReportElementService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	@Autowired
	private TaskCompletionsLocksService taskCompletionsLocksService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private TicketDownloadService ticketDownloadService;
	
	
	public GenericResponse<ExpenseReportDTO> createNewReport(ExpenseReportCreationRequest req){
		if(req==null || StringUtility.isEmptyOrWhitespace(req.getTitle()) || req.getDateOfExpence()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		Calendar date = DateUtility.dateToCalendar(req.getDateOfExpence());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		
		if(profile.getDateOfEmployment()!=null) {
			if(date.before(profile.getDateOfEmployment())) {
				return this.buildUnprocessableEntity(StandardReturnCodesEnum.USER_IS_NOT_AN_EMPLOYEE);
			}
		}
		
		ExpenseReport report = new ExpenseReport();
		report.setAmount(0d);
		report.setAmountAccepted(0d);
		report.setDateOfExpence(req.getDateOfExpence());
		report.setLocation(req.getLocation());
		report.setMadeBy(profile);
		report.setStatus(ExpenseReportStatusEnum.TO_BE_PROCESSED.name());
		report.setTitle(StringUtils.trim(req.getTitle()));
		
		if(req.getTaskCode()==null) {
			report.setWorkTask(null);
		}else {
			WorkTask task = this.workTaskService.findByTaskCode(req.getTaskCode());
			
			if(task==null) {
				return this.buildNotFoundResponse("Task not found");
			}
			
			report.setWorkTask(task);
		}
		
		
		if(!report.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		report = this.expenseReportService.saveOrUpdate(report);
		
		return this.buildOkResponse(ExpenseReportTransformer.mapToDTO(report));
	}
	
	
	public GenericResponse<List<ExpenseReportElementDTO>> findAllExpencesOfReport(Long reportId){
		if(reportId==null) {
			return this.buildBadDataResponse();
		}
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		//check that user is the owner
		ExpenseReport report = this.expenseReportService.findByIdAndUser(profile, reportId);
		
		if(report==null) {
			return this.buildNotFoundResponse();
		}
		
		Sort sort = Sort.by(Direction.DESC, "id");
		List<ExpenseReportElement> list = this.expenseReportElementService.findAllExpensesInReport(report, sort);
		
		List<ExpenseReportElementDTO> res = ExpenseReportElementTransformer.mapToDTO(list);
		
		
		return this.buildOkResponse(res);
	}
	
	

	public GenericResponse<Page<ExpenseReportDTO>> findAllExpenseReportOfUsers(
			String madeByEmail,  String madeByName, String madeBySurname, String status, String location, 
			String title, Date dateOfExpenceFrom, Date dateOfExpenceTo, Pageable pageable){
		if(pageable==null) {
			return this.buildBadDataResponse();
		}
				
		Pair<List<ExpenseReport>, Long> pair = this.expenseReportService.findAllAndCount(null, madeByName, madeBySurname, status, 
				madeByEmail, dateOfExpenceFrom, dateOfExpenceTo, location, title, pageable);
		
		long totalCount = pair.getValue();
		
		List<ExpenseReportDTO> list = ExpenseReportTransformer.mapToDTO(pair.getKey());
				
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}
	
	
	public GenericResponse<Page<ExpenseReportDTO>> findAllExpenceReportOfCurrentLoggedUser(
			String status, String location, String title, Date dateOfExpenceFrom, Date dateOfExpenceTo, Pageable pageable){
		if(pageable==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		Pair<List<ExpenseReport>, Long> pair = this.expenseReportService.findAllAndCount(profile, null, null, status, 
														null, dateOfExpenceFrom, dateOfExpenceTo, location, title, pageable);
		
		long totalCount = pair.getValue();
		
		List<ExpenseReportDTO> list = ExpenseReportTransformer.mapToDTO(pair.getKey());
		
		
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}

	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST})
	public GenericResponse<ExpenseReportElementDTO> addNewReportElementToMyReport(ReportElementAddRequest element,
			MultipartFile attachment) throws Exception {
		if(element==null || element.getAmount()==null || element.getAmount().doubleValue()<0d || 
				StringUtility.isEmptyOrWhitespace(element.getDescription()) || element.getExpenseReportId()==null) {
			return this.buildBadDataResponse();
		}
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		//check that user is the owner
		ExpenseReport report = this.expenseReportService.findByIdAndUser(profile, element.getExpenseReportId());
		
		if(report==null) {
			return this.buildNotFoundResponse();
		}
		
		Calendar date = DateUtility.dateToCalendar(report.getDateOfExpence());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		
		if(!StringUtils.equals(report.getStatus(), ExpenseReportStatusEnum.TO_BE_PROCESSED.name())){
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.REPORT_CANNOT_BE_MODIFIED);
		}
		
		Document reportElemDoc = null;
		
		if(attachment!=null) {
			reportElemDoc = this.documentService.saveFileWithDefaultCryptedFM(attachment.getOriginalFilename(), 
					attachment.getInputStream(), profile.getId(), StandardDocumentDescription.PAYCHECK.name());
		}
		
		
		
		ExpenseReportElement elem = new ExpenseReportElement();
		elem.setAccepted(null);
		elem.setAmount(element.getAmount());
		elem.setDescription(StringUtils.trim(element.getDescription()));
		elem.setExpenseReport(report);
		elem.setAttachment(reportElemDoc);
		
		if(!elem.canBeInsertedInDatabase()) {
			//clear file created
			if(reportElemDoc!=null) {
				this.documentService.deleteOnlyFile(reportElemDoc);
			}			
			
			return this.buildBadDataResponse();
		}
		
		report.setAmount(report.getAmount() +elem.getAmount());
		report = this.expenseReportService.saveOrUpdate(report);
		elem.setExpenseReport(report);
		
		elem = this.expenseReportElementService.saveOrUpdate(elem);
		
		
		return this.buildOkResponse(ExpenseReportElementTransformer.mapToDTO(elem));
	}
	
	
	public GenericResponse<ExpenseReportDetailsDTO> findUserExpenseReportDetails(Long expenseId) {
		if(expenseId==null) {
			return this.buildBadDataResponse();
		}
		ExpenseReport report = this.expenseReportService.findById(expenseId);
		
		if(report==null) {
			return this.buildNotFoundResponse();
		}
		Sort sort = Sort.by(Direction.DESC, "id");
		
		List<ExpenseReportElement> list = this.expenseReportElementService.findAllExpensesInReport(report, sort);
		
		List<ExpenseReportElementDTO> expensesDTO = ExpenseReportElementTransformer.mapToDTO(list);
		ExpenseReportDTO reportDTO = ExpenseReportTransformer.mapToDTO(report);
		
		ExpenseReportDetailsDTO dto = new ExpenseReportDetailsDTO();
		dto.setExpenses(expensesDTO);
		dto.setReport(reportDTO);
	
		return this.buildOkResponse(dto);
	}
	
	public GenericResponse<ExpenseReportDetailsDTO> findMyExpenseReportDetails(Long expenseId) {
		if(expenseId==null) {
			return this.buildBadDataResponse();
		}
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		//check that user is the owner
		ExpenseReport report = this.expenseReportService.findByIdAndUser(profile, expenseId);
		
		if(report==null) {
			return this.buildNotFoundResponse();
		}
		
		Sort sort = Sort.by(Direction.DESC, "id");
		
		List<ExpenseReportElement> list = this.expenseReportElementService.findAllExpensesInReport(report, sort);
		
		List<ExpenseReportElementDTO> expensesDTO = ExpenseReportElementTransformer.mapToDTO(list);
		ExpenseReportDTO reportDTO = ExpenseReportTransformer.mapToDTO(report);
		
		ExpenseReportDetailsDTO dto = new ExpenseReportDetailsDTO();
		dto.setExpenses(expensesDTO);
		dto.setReport(reportDTO);
	
		return this.buildOkResponse(dto);
	}

	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST})
	public GenericResponse<ExpenseReportDTO> updateReport(ExpenseReportUpdateRequest updateReq) {
		if(updateReq==null || updateReq.getExpenseReportId()==null || StringUtility.isEmptyOrWhitespace(updateReq.getTitle()) || 
				updateReq.getDateOfExpence()==null) {
			return this.buildBadDataResponse();
		}
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		Calendar date = DateUtility.dateToCalendar(updateReq.getDateOfExpence());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		
		if(profile.getDateOfEmployment()!=null) {
			if(date.before(profile.getDateOfEmployment())) {
				return this.buildUnprocessableEntity(StandardReturnCodesEnum.USER_IS_NOT_AN_EMPLOYEE);
			}
		}
		
		//check that user is the owner
		ExpenseReport report = this.expenseReportService.findByIdAndUser(profile, updateReq.getExpenseReportId());
		
		if(report==null) {
			return this.buildNotFoundResponse();
		}
		
		if(!StringUtils.equals(report.getStatus(), ExpenseReportStatusEnum.TO_BE_PROCESSED.name())){
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.REPORT_CANNOT_BE_MODIFIED);
		}
		
		if(updateReq.getTaskCode()==null) {
			report.setWorkTask(null);
		}else {
			WorkTask task = this.workTaskService.findByTaskCode(updateReq.getTaskCode());
			
			if(task==null) {
				return this.buildNotFoundResponse("Task not found");
			}
			
			report.setWorkTask(task);
		}
		
		
		report.setDateOfExpence(updateReq.getDateOfExpence());
		report.setLocation(updateReq.getLocation());
		report.setTitle(StringUtils.trim(updateReq.getTitle()));
		
		if(!report.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		report = this.expenseReportService.saveOrUpdate(report);
		
		return this.buildOkResponse(ExpenseReportTransformer.mapToDTO(report));
	}
	
	

	public GenericResponse<TicketDownloadDTO> downloadExpenseOfUserReportAttachment(Long expenseId) {
		if(expenseId==null) {
			return this.buildBadDataResponse();
		}
		
		ExpenseReportElement elem = this.expenseReportElementService.findById(expenseId);
		
		if(elem==null) {
			return this.buildNotFoundResponse();
		}
		
		TicketDownload ticketDownlod = this.ticketDownloadService.createAndSaveOneShotTicketForDocument(elem.getAttachment());
		
		
		return this.buildOkResponse(TicketDownloadTransformer.mapToDTO(ticketDownlod));
	}
	
	public GenericResponse<TicketDownloadDTO> downloadExpenseOfReportAttachment(Long expenseId) {
		if(expenseId==null) {
			return this.buildBadDataResponse();
		}
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		ExpenseReportElement elem = this.expenseReportElementService.findById(expenseId);
		
		if(elem==null) {
			return this.buildNotFoundResponse();
		}
		
		//NOT OWNED BY THIS USER
		if(!elem.getExpenseReport().getMadeBy().getId().equals(profile.getId())){
			return this.buildNotFoundResponse();
		}
		
		TicketDownload ticketDownlod = this.ticketDownloadService.createAndSaveOneShotTicketForDocument(elem.getAttachment());
		
		
		return this.buildOkResponse(TicketDownloadTransformer.mapToDTO(ticketDownlod));
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> deleteExpenseOfReport(Long expenseId) throws Exception {
		if(expenseId==null) {
			return this.buildBadDataResponse();
		}
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		ExpenseReportElement elem = this.expenseReportElementService.findById(expenseId);
		
		if(elem==null) {
			return this.buildNotFoundResponse();
		}
		
		//NOT OWNED BY THIS USER
		if(!elem.getExpenseReport().getMadeBy().getId().equals(profile.getId())){
			return this.buildNotFoundResponse();
		}
		
		ExpenseReport report = elem.getExpenseReport();
		
		Calendar date = DateUtility.dateToCalendar(report.getDateOfExpence());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		if(!StringUtils.equals(report.getStatus(), ExpenseReportStatusEnum.TO_BE_PROCESSED.name())){
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.REPORT_CANNOT_BE_MODIFIED);
		}
		
		if(BooleanUtils.isTrue(elem.getAccepted())) {
			report.setAmountAccepted(report.getAmountAccepted() - elem.getAmount());
		}
		
		report.setAmount(report.getAmount() - elem.getAmount());
		this.expenseReportService.saveOrUpdate(report);
		
		this.documentService.deleteDocumentAndFile(elem.getAttachment());
		
		this.expenseReportElementService.delete(elem);
		
		return this.buildStringOkResponse("successfully deleted");
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> deleteReport(Long reportId) throws Exception {
		if(reportId==null) {
			return this.buildBadDataResponse();
		}
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		//check that user is the owner
		ExpenseReport report = this.expenseReportService.findByIdAndUser(profile, reportId);
		
		if(report==null) {
			return this.buildNotFoundResponse();
		}
		
		Calendar date = DateUtility.dateToCalendar(report.getDateOfExpence());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		if(!StringUtils.equals(report.getStatus(), ExpenseReportStatusEnum.TO_BE_PROCESSED.name())){
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.REPORT_CANNOT_BE_MODIFIED);
		}
		
		Sort sort = Sort.by(Direction.DESC, "id");
		List<ExpenseReportElement> elements = this.expenseReportElementService.findAllExpensesInReport(report, sort);
		
		for (ExpenseReportElement expenseReportElement : elements) {
			this.documentService.deleteDocumentAndFile(expenseReportElement.getAttachment());
			
			this.expenseReportElementService.delete(expenseReportElement);
		}
		
		this.expenseReportService.delete(report);
		
		return this.buildStringOkResponse("successfully deleted");
	}

	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<ExpenseReportElementDTO> acceptExpenseReportElement(Long reportElementId) {
		if(reportElementId==null) {
			return this.buildBadDataResponse();
		}
		
		ExpenseReportElement elem = this.expenseReportElementService.findById(reportElementId);
		
		if(elem==null) {
			return this.buildNotFoundResponse();
		}
		
		ExpenseReport report = elem.getExpenseReport();
		
		Calendar date = DateUtility.dateToCalendar(report.getDateOfExpence());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		if(!StringUtils.equals(report.getStatus(), ExpenseReportStatusEnum.PROCESSING.name())) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.EXPENSE_REPORT_NOT_IN_PROCESSING_STATUS);
		}
		
		//was previously accepted or refused
		if(elem.getAccepted()!=null) {
			//if is not in accepted status
			if(BooleanUtils.isFalse(elem.getAccepted())) {
				report.setAmountAccepted(report.getAmountAccepted() + elem.getAmount());
				elem.setAccepted(true);
				this.expenseReportElementService.saveOrUpdate(elem);
				this.expenseReportService.saveOrUpdate(report);
			}
			//ignore if was already accepted
		}else {
			//was not previously accepted or refused
			report.setAmountAccepted(report.getAmountAccepted() + elem.getAmount());
			elem.setAccepted(true);
			this.expenseReportElementService.saveOrUpdate(elem);
			this.expenseReportService.saveOrUpdate(report);
		}
		
		return this.buildOkResponse(ExpenseReportElementTransformer.mapToDTO(elem));
	}

	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<ExpenseReportElementDTO> refuseExpenseReportElement(Long reportElementId) {
		if(reportElementId==null) {
			return this.buildBadDataResponse();
		}
		
		ExpenseReportElement elem = this.expenseReportElementService.findById(reportElementId);
		
		if(elem==null) {
			return this.buildNotFoundResponse();
		}
		
		ExpenseReport report = elem.getExpenseReport();
		
		Calendar date = DateUtility.dateToCalendar(report.getDateOfExpence());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		if(!StringUtils.equals(report.getStatus(), ExpenseReportStatusEnum.PROCESSING.name())) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.EXPENSE_REPORT_NOT_IN_PROCESSING_STATUS);
		}
		
		//was previously accepted or refused
		if(elem.getAccepted()!=null) {
			//if is in accepted status
			if(BooleanUtils.isTrue(elem.getAccepted())) {
				report.setAmountAccepted(report.getAmountAccepted() - elem.getAmount());
				elem.setAccepted(false);
				this.expenseReportElementService.saveOrUpdate(elem);
				this.expenseReportService.saveOrUpdate(report);
			}
			//ignore if was already refused
		}else {
			//was not previously accepted or refused
			elem.setAccepted(false);
			this.expenseReportElementService.saveOrUpdate(elem);
		}
		
		return this.buildOkResponse(ExpenseReportElementTransformer.mapToDTO(elem));
	}


	public GenericResponse<ExpenseReportDTO> updateExpenseReport(ExpenseReportUpdateRequestByExpenseManager updateReq) {
		if(updateReq==null || updateReq.getExpenseReportId()==null || updateReq.getStatus()==null ||
				!ExpenseReportStatusEnum.isValid(updateReq.getStatus())) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		ExpenseReport report = this.expenseReportService.findById(updateReq.getExpenseReportId());
		
		if(report==null) {
			return this.buildNotFoundResponse();
		}
	
		Calendar date = DateUtility.dateToCalendar(report.getDateOfExpence());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		String oldStatus = report.getStatus();
		
		if(StringUtils.equals(updateReq.getStatus(), ExpenseReportStatusEnum.PROCESSING.name())) {
			report.setProcessingBy(profile);
			report.setProcessedBy(null);
		}
		
		if(StringUtils.equals(updateReq.getStatus(), ExpenseReportStatusEnum.ACCEPTED.name()) || 
				StringUtils.equals(updateReq.getStatus(), ExpenseReportStatusEnum.PARTIALLY_ACCEPTED.name()) ||
					StringUtils.equals(updateReq.getStatus(), ExpenseReportStatusEnum.REFUSED.name())) {
			if(report.getProcessingBy()==null) {
				report.setProcessingBy(profile);
			}
			report.setProcessedBy(profile);
		}
		
		if(StringUtils.equals(updateReq.getStatus(), ExpenseReportStatusEnum.TO_BE_PROCESSED.name())) {
			report.setProcessedBy(null);
			report.setProcessingBy(null);
		}
		
		report.setStatus(updateReq.getStatus());
		report.setNotes(updateReq.getNotes());
		
		report = this.expenseReportService.saveOrUpdate(report);
		
		return this.buildOkResponse(ExpenseReportTransformer.mapToDTO(report));
	}


	
	
	
}
