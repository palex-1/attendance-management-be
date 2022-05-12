package it.palex.attendanceManagement.core.service.turnstile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.palex.attendanceManagement.auth.dto.ExportAttendanceRequestDTO;
import it.palex.attendanceManagement.commons.utils.document.MultipleDocumentBuilderFactory;
import it.palex.attendanceManagement.commons.utils.tables.Table;
import it.palex.attendanceManagement.data.dto.documents.TicketDownloadDTO;
import it.palex.attendanceManagement.data.dto.transformers.TicketDownloadTransformer;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.TicketDownload;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.data.service.documento.TicketDownloadService;
import it.palex.attendanceManagement.library.service.ConfigurationsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.core.dtos.turnstile.UserAttendanceAddRequest;
import it.palex.attendanceManagement.core.dtos.turnstile.UserAttendanceAddSwitchedRequest;
import it.palex.attendanceManagement.data.dto.core.UserAttendanceDTO;
import it.palex.attendanceManagement.data.dto.transformers.core.UserAttendanceTransformer;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.Turnstile;
import it.palex.attendanceManagement.data.entities.core.UserAttendance;
import it.palex.attendanceManagement.data.entities.enumTypes.TurnstileTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.UserAttendanceTypeEnum;
import it.palex.attendanceManagement.data.service.core.TurnstileService;
import it.palex.attendanceManagement.data.service.core.UserAttendanceService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Service
public class UserAttendanceWebService implements GenericService {

	@Value("${turnstile.min-sec-to-wait-to-add-new-attendance:60}")
	private int minSecondsToWaitToAddNewAttendance;

	@Autowired
	private UserAttendanceService userAttendanceService;

	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private TurnstileService turnstileService;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private TicketDownloadService ticketDownloadService;

	@Autowired
	private ConfigurationsService configurationsService;


	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<UserAttendanceDTO> addNewAttendance(UserAttendanceAddRequest attendance){
		if(attendance==null || attendance.getTurnstileId()==null || attendance.getType()==null 
					|| attendance.getUserProfileId()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.userProfileService.findById(attendance.getUserProfileId());
		
		if(profile==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.USER_NOT_FOUND);
		}
		
		Turnstile turnstile = this.turnstileService.findById(attendance.getTurnstileId());
		
		if(turnstile==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.TURNSTILE_NOT_FOUND);
		}
		
		if(turnstile.getDeactivated()) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.TURNSTILE_NOT_ACTIVE); 
		}
		
		if(!StringUtils.equals(turnstile.getType(), TurnstileTypeEnum.VIRTUAL.name())) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.TURNSTILE_IS_NOT_VIRTUAL);
		}
		
		
		//if date is not specified use current time
		Date datePark = attendance.getTimestamp()!=null ? attendance.getTimestamp():DateUtility.getCurrentDateInUTC();
		
		UserAttendance toAdd = new UserAttendance();
		toAdd.setDeleted(false);
		toAdd.setTimestamp(datePark);
		toAdd.setTurnstile(turnstile);
		toAdd.setType(attendance.getType());
		toAdd.setUserProfile(profile);
		
		if(!toAdd.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		toAdd = this.userAttendanceService.saveOrUpdate(toAdd);
		
		UserAttendanceDTO dto = UserAttendanceTransformer.mapToDTO(toAdd);
		
		return this.buildOkResponse(dto);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<UserAttendanceDTO> addNewAttendanceSwitched(UserAttendanceAddSwitchedRequest attendance) {
		if(attendance==null || attendance.getTurnstileId()==null || attendance.getUserProfileId()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.userProfileService.findById(attendance.getUserProfileId());
		
		if(profile==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.USER_NOT_FOUND);
		}
		
		Turnstile turnstile = this.turnstileService.findById(attendance.getTurnstileId());
		
		if(turnstile==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.TURNSTILE_NOT_FOUND);
		}
		
		if(turnstile.getDeactivated()) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.TURNSTILE_NOT_ACTIVE); 
		}
		
		if(!StringUtils.equals(turnstile.getType(), TurnstileTypeEnum.VIRTUAL.name())) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.TURNSTILE_IS_NOT_VIRTUAL);
		}
		
		
		Date datePark = DateUtility.getCurrentDateInUTC();
		
		UserAttendance old = this.userAttendanceService.findLastAttendanceOfUser(profile);
		
		UserAttendanceTypeEnum attendanceType = UserAttendanceTypeEnum.ENTER;
		
		if(old!=null) {
			long diffInSeconds = DateUtility.diffInSeconds(datePark, old.getTimestamp());
			diffInSeconds = Math.abs(diffInSeconds);
			
			if(diffInSeconds < this.minSecondsToWaitToAddNewAttendance) {
				return this.buildEnhanceYourCalmResponse(StandardReturnCodesEnum.WAIT_TO_ADD_NEW_ATTENDANCE);
			}
			
			if(StringUtils.equals(old.getType(), UserAttendanceTypeEnum.ENTER.name())) {
				attendanceType = UserAttendanceTypeEnum.EXIT;
			}
		}
		
		UserAttendance toAdd = new UserAttendance();
		toAdd.setDeleted(false);
		toAdd.setTimestamp(datePark);
		toAdd.setTurnstile(turnstile);
		toAdd.setType(attendanceType.name());
		toAdd.setUserProfile(profile);
		
		if(!toAdd.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		toAdd = this.userAttendanceService.saveOrUpdate(toAdd);
		
		UserAttendanceDTO dto = UserAttendanceTransformer.mapToDTO(toAdd);
		
		return this.buildOkResponse(dto);
	}
	
	
	

	public GenericResponse<Page<UserAttendanceDTO>> findAllAttendance(Date dateFrom, Date dateTo,
			Long turnstileId, Pageable pageable){
		if(turnstileId==null || pageable==null) {
			return this.buildBadDataResponse();
		}
		
		Turnstile turnstile = this.turnstileService.findById(turnstileId);
		
		if(turnstile==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.TURNSTILE_NOT_FOUND);
		}
		
		Date startDate = dateFrom==null ? null : DateUtility.startOfDayOfDate(dateFrom);
		Date endDate = dateTo==null ? null : DateUtility.startOfDayOfDate(dateTo);
		
		Pair<List<UserAttendance>, Long> attedanceAndCount = this.userAttendanceService
				.findAllAttendanceOfTurnstile(turnstile, startDate, endDate, pageable);
		
		long totalCount = attedanceAndCount.getValue();
		List<UserAttendanceDTO> list = UserAttendanceTransformer.mapToDTO(attedanceAndCount.getKey());
		
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}
	
	
	
	public GenericResponse<StringDTO> deleteAttendance(Long attendanceId){
		if(attendanceId==null) {
			return this.buildBadDataResponse();
		}
		UserAttendance attendance = this.userAttendanceService.findById(attendanceId);
		
		if(attendance==null) {
			return this.buildNotFoundResponse();
		}
		
		this.userAttendanceService.deleteAttendance(attendance);
		
		return this.buildStringOkResponse("Successfully deleted");
	}



	public GenericResponse<List<UserAttendanceDTO>> findAllAttendanceOfMonth(Integer year, Integer month,
			Integer userProfileId) {
		if(year==null || month==null || userProfileId==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.userProfileService.findById(userProfileId);
		
		if(profile==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.USER_NOT_FOUND);
		}
		
		Calendar startRange = Calendar.getInstance();
		startRange.set(Calendar.YEAR, year);
		startRange.set(Calendar.MONTH, month);
		
		startRange.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endRange = Calendar.getInstance();
		endRange.set(Calendar.YEAR, year);
		endRange.set(Calendar.MONTH, month);
		int lastDayOfMonth = startRange.getActualMaximum(Calendar.DAY_OF_MONTH);
		endRange.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
		
		Sort sort = Sort.by(Direction.ASC, "timestamp");
		
		List<UserAttendance> attendances = this.userAttendanceService.findTaskOfUserInDateRange(
				profile, sort, DateUtility.startOfDayOfDate(startRange.getTime()), 
				DateUtility.endOfDayOfDate(endRange.getTime()));
		
		List<UserAttendanceDTO> list = UserAttendanceTransformer.mapToDTO(attendances);
		
		
		return this.buildOkResponse(list);
	}


    public GenericResponse<TicketDownloadDTO> exportAttendanceOfDay(ExportAttendanceRequestDTO req)
			throws Exception {
		if(req==null || req.getDay()==null){
			return this.buildBadDataResponse();
		}

		if(!DailyAttendanceTableGenerator.isSupportedLocale(req.getExportLocale())){
			return this.buildBadDataResponse("Not supported locale");
		}

		Sort sort = Sort.by(
				Sort.Order.asc("userProfile.id"),
				Sort.Order.asc("timestamp"));

		List<UserAttendance> attendances = this.userAttendanceService
				.findAttendanceOfDay(req.getTurnstileId(), req.getDay(), sort);

		ArrayList<Table> sheets = DailyAttendanceTableGenerator.createExportAttendance(req.getExportLocale(), attendances);
		InputStream fileStream = MultipleDocumentBuilderFactory.build(MultipleDocumentBuilderFactory.XLSBuilderID, sheets);

		String fileExt = MultipleDocumentBuilderFactory.XLSBuilderID;

		Document savedDocument = documentService.saveTempFileWithDefaultFM(fileExt, fileStream);

		// token validity is equal to the time uses to delete file
		int tokenValidity = this.configurationsService.getTempFileTimeToLiveSeconds();
		TicketDownload ticket = this.ticketDownloadService
										.createAndSaveTicketForDocument(savedDocument, tokenValidity);

		TicketDownloadDTO res = TicketDownloadTransformer.mapToDTO(ticket);

		return this.buildOkResponse(res);
    }


}
