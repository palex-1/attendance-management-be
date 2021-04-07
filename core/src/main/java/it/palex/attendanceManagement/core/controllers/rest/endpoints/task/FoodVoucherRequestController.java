package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.dtos.tasks.FoodVoucherRequestAdd;
import it.palex.attendanceManagement.core.service.userProfile.FoodVoucherRequestWebService;
import it.palex.attendanceManagement.data.dto.core.FoodVoucherRequestDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.BooleanDTO;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping(path="foodVoucher")
public class FoodVoucherRequestController extends RestEndpoint {

	@Autowired
	private FoodVoucherRequestWebService foodVoucherRequestWebService;
	
	@GetMapping(path="foodVoucherEnabled")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<BooleanDTO>> checkFoodVoucherEnabled() {
		GenericResponse<BooleanDTO> res = this.foodVoucherRequestWebService
				.checkFoodVoucherEnabled();
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path="findByDate")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<FoodVoucherRequestDTO>> findByDate(
			@RequestParam(name = "day", required = true) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime day) {
		
		ZonedDateTime zonedDateTime = day.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());
        
		GenericResponse<FoodVoucherRequestDTO> res = this.foodVoucherRequestWebService
				.findByDate(date);
		
		return this.buildGenericResponse(res);
	}
	
	
	@PostMapping(path="create")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<FoodVoucherRequestDTO>> create(
			@RequestBody FoodVoucherRequestAdd addReq) {
		
		GenericResponse<FoodVoucherRequestDTO> res = this.foodVoucherRequestWebService
				.addFoodVoucherRequest(addReq);
		
		return this.buildGenericResponse(res);
	}
	
	
	@DeleteMapping(path="delete")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> delete(
			@RequestParam(name = "foodVoucherId", required = true) Long foodVoucherReqId) {
		
		GenericResponse<StringDTO> res = this.foodVoucherRequestWebService
				.removeFoodVoucherRequest(foodVoucherReqId);
		
		return this.buildGenericResponse(res);
	}
	
	
}
