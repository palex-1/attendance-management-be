package it.palex.attendanceManagement.core.service.turnstile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.core.dtos.turnstile.NameSurnameIdUsersDTO;
import it.palex.attendanceManagement.core.dtos.turnstile.UserAttendanceTurnstileAddRequest;
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
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Service
public class TurnstileAttendanceWebService implements GenericService {

	@Autowired
	private UserAttendanceService userAttendanceService;

	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private TurnstileService turnstileService;
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<UserAttendanceDTO> addNewAttendance(String turnstileToken,
			UserAttendanceTurnstileAddRequest attendance){
		if(turnstileToken==null) {
			return this.buildUnauthorizedResponse();
		}
		
		Turnstile turnstile = this.turnstileService.findByAuthToken(turnstileToken);
		
		if(turnstile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		if(attendance==null  || attendance.getType()==null || attendance.getUserProfileId()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.userProfileService.findById(attendance.getUserProfileId());
		
		if(profile==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.USER_NOT_FOUND);
		}
		
		if(turnstile.getDeactivated()) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.TURNSTILE_NOT_ACTIVE); 
		}
		
		if(!StringUtils.equals(turnstile.getType(), TurnstileTypeEnum.PHYSICAL.name())) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.TURNSTILE_IS_NOT_PHYSICAL);
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


	public GenericResponse<NameSurnameIdUsersDTO> findAllUserNameAndId(String turnstileToken) {
		if(turnstileToken==null) {
			return this.buildUnauthorizedResponse();
		}
		Turnstile turnstile = this.turnstileService.findByAuthToken(turnstileToken);
		
		if(turnstile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		///TODO FIXME get users using pagination
		List<UserProfile> allUsers = this.userProfileService.findAllUserWithActiveAccount(
					PageRequest.of(0, Integer.MAX_VALUE)
				);
		
		NameSurnameIdUsersDTO res = new NameSurnameIdUsersDTO();
		
		Map<Integer, String> userNameIdMap = new HashMap<>();
			
		for(UserProfile profile: allUsers) {
			String nameSurname = profile.getName()+" "+profile.getSurname();
			
			userNameIdMap.put(profile.getId(), nameSurname);
		}
		
		res.setUserNameIdMap(userNameIdMap);
		
		return this.buildOkResponse(res);
	}
	
}
