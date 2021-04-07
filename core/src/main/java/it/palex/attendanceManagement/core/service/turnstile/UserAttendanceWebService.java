package it.palex.attendanceManagement.core.service.turnstile;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.core.dtos.turnstile.UserAttendanceAddRequest;
import it.palex.attendanceManagement.data.dto.core.UserAttendanceDTO;
import it.palex.attendanceManagement.data.dto.transformers.core.UserAttendanceTransformer;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.Turnstile;
import it.palex.attendanceManagement.data.entities.core.UserAttendance;
import it.palex.attendanceManagement.data.entities.enumTypes.TurnstileTypeEnum;
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
	
	@Autowired
	private UserAttendanceService userAttendanceService;

	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private TurnstileService turnstileService;
	
	
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
	
	
}
