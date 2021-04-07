package it.palex.attendanceManagement.core.service.userProfile;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.core.dtos.tasks.FoodVoucherRequestAdd;
import it.palex.attendanceManagement.data.dto.core.FoodVoucherRequestDTO;
import it.palex.attendanceManagement.data.dto.transformers.core.FoodVoucherTransformer;
import it.palex.attendanceManagement.data.entities.FoodVoucherRequest;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.exceptions.InvalidConfigurationException;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.core.FoodVoucherRequestService;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.core.TaskCompletionsLocksService;
import it.palex.attendanceManagement.data.service.incarico.CompletedTaskService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.BooleanDTO;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Service
public class FoodVoucherRequestWebService implements GenericService{

	@Autowired
	private FoodVoucherRequestService foodVoucherRequestService;
	
	@Autowired
	private CompletedTaskService completedTaskService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	@Autowired
	private GlobalConfigurationsService globalConfigurationsService;
	
	@Autowired
	private TaskCompletionsLocksService taskCompletionsLocksService;
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<FoodVoucherRequestDTO> addFoodVoucherRequest(FoodVoucherRequestAdd addReq){
		if(addReq==null || addReq.getDay()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		Calendar date = DateUtility.dateToCalendar(addReq.getDay());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		if(user.getDateOfEmployment()!=null) {
			if(date.before(user.getDateOfEmployment())) {
				return this.buildUnprocessableEntity(StandardReturnCodesEnum.USER_IS_NOT_AN_EMPLOYEE);
			}
		}
		
		FoodVoucherRequest oldReq = this.foodVoucherRequestService.findByDayAndUser(addReq.getDay(), user);
		
		if(oldReq!=null) {
			return this.buildOkResponse(FoodVoucherTransformer.mapToDTO(oldReq), StandardReturnCodesEnum.ALREADY_ADDED_FOOD_VOUCHER_REQUEST);
		}
		
		long workedHours = this.completedTaskService.sumOnlyWorkedHoursMadeOnDay(user, addReq.getDay());
		
		int hoursToRequestFoodVoucher = getHoursToEnableFoodVoucherRequest();
		
		if(workedHours<hoursToRequestFoodVoucher) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.NOT_ENOUGHT_WORKED_HOURS_TO_REQUEST_FOOD_VOUCHER);
		}
		
		FoodVoucherRequest voucher = new FoodVoucherRequest();
		voucher.setEditable(true);
		voucher.setQuantity(1);
		voucher.setDay(addReq.getDay());
		voucher.setUserProfile(user);

		voucher = this.foodVoucherRequestService.saveOrUpdate(voucher);
		
		return this.buildOkResponse(FoodVoucherTransformer.mapToDTO(voucher));
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> removeFoodVoucherRequest(Long foodVoucherReqId){
		if(foodVoucherReqId==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		FoodVoucherRequest toDelete = this.foodVoucherRequestService.findByIdAndUser(foodVoucherReqId, user);
		
		if(toDelete==null) {
			return this.buildStringOkResponse("Already deleted"); //do not give error
		}
		
		Calendar date = DateUtility.dateToCalendar(toDelete.getDay());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		
		this.foodVoucherRequestService.delete(toDelete);
		
		return this.buildStringOkResponse("Successfully deleted");
	}
	
	
	public GenericResponse<FoodVoucherRequestDTO> findByDate(Date day) {
		if(day==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile user = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(user==null) {
			return this.buildUnauthorizedResponse();
		}
		
		FoodVoucherRequest req = this.foodVoucherRequestService.findByDayAndUser(day, user);
		
		return this.buildOkResponse(FoodVoucherTransformer.mapToDTO(req));
	}
	
	/**
	 * This methos will sanitize food voucher requests. If the user has not enought hours to request food voucher
	 * if a food voucher is present, this will be deleted.
	 * This can happen when the user changes the worked hours after food voucher request
	 * @param day
	 */
	public void sanitizeFoodVoucherRequest(UserProfile user, Date day) {
		if(day==null) {
			throw new NullPointerException();
		}
		long workedHours = this.completedTaskService.sumOnlyWorkedHoursMadeOnDay(user, day);
		
		int hoursToRequestFoodVoucher = getHoursToEnableFoodVoucherRequest();
		
		//If the user has not enought hours to request food voucher
		if(workedHours<hoursToRequestFoodVoucher) {
			//delete voucher if exists
			FoodVoucherRequest request = this.foodVoucherRequestService.findByDayAndUser(day, user);
			if(request!=null) {
				this.foodVoucherRequestService.delete(request);
			}
		}
	}
	
	private int getHoursToEnableFoodVoucherRequest() {
		String config = this.globalConfigurationsService.getConfigValue(GlobalConfigurationSettingsTuple.FOOD_VOURCHER.AREA_NAME, 
						GlobalConfigurationSettingsTuple.FOOD_VOURCHER.MIN_WORKED_HOURS_TO_REQUEST_IT);
		if(config==null) {
			throw new InvalidConfigurationException(
					"Not found config --> area="+GlobalConfigurationSettingsTuple.FOOD_VOURCHER.AREA_NAME+","
					+ "key="+GlobalConfigurationSettingsTuple.FOOD_VOURCHER.MIN_WORKED_HOURS_TO_REQUEST_IT);
		}
		
		try {
			return Integer.parseInt(config.trim());
			
		}catch(NumberFormatException e) {
			throw new InvalidConfigurationException(
					"Invalid config --> area="+GlobalConfigurationSettingsTuple.FOOD_VOURCHER.AREA_NAME+","
					+ "key="+GlobalConfigurationSettingsTuple.FOOD_VOURCHER.MIN_WORKED_HOURS_TO_REQUEST_IT);
		}
	}

	public GenericResponse<BooleanDTO> checkFoodVoucherEnabled() {
		String config = this.globalConfigurationsService.getConfigValue(GlobalConfigurationSettingsTuple.FOOD_VOURCHER.AREA_NAME, 
				GlobalConfigurationSettingsTuple.FOOD_VOURCHER.ENABLED);
		
		boolean enabled = BooleanUtils.toBoolean(config);
		
		BooleanDTO res = new BooleanDTO();
		res.setValue(enabled);
		
		return this.buildOkResponse(res);
	}

	
	
	
	
	
	
	
	
	
	
	
}
