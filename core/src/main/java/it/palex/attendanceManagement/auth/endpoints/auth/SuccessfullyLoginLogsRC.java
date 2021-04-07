/**
 * 
 */
package it.palex.attendanceManagement.auth.endpoints.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.data.dto.security.SuccessfullyLoginLogsDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.data.service.user.SuccessfullyLoginLogsService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;

/**
 * @author Alessandro Pagliaro
 *
 */
@RestController
@RequestMapping("/succLoginLogs")
public class SuccessfullyLoginLogsRC extends RestEndpoint{

	
	@Autowired
	private SuccessfullyLoginLogsService succLogsSrv;

	@GetMapping
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	public ResponseEntity<GenericResponse<Page<SuccessfullyLoginLogsDTO>>> findAll(
			@PageableDefault(page = 0, size = 5, sort={"successfullyLoginLogsPK.loginDate"},direction=Direction.DESC) Pageable pageable,
			@RequestParam(value="fromDate", required=false) 
				@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date fromDate,
			@RequestParam(value="toDate", required=false) 
				@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date toDate){
		try{
			GenericResponse<Page<SuccessfullyLoginLogsDTO>> response = 
					this.succLogsSrv.findAll(pageable, fromDate, toDate);
			
			return this.buildGenericResponse(response);
		}
		catch(Exception e){
			return this.buildInternalErrorResponse(e);
		}
	}

}
