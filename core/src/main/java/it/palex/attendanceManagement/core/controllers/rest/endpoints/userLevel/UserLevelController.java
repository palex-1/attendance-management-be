package it.palex.attendanceManagement.core.controllers.rest.endpoints.userLevel;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.service.userLevel.UserLevelWebService;
import it.palex.attendanceManagement.data.dto.core.UserLevelDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 24 giu 2020
 */
@RestController
@RequestMapping(path="userLevels")
public class UserLevelController extends RestEndpoint {

	@Autowired
	private UserLevelWebService userLevelWebService;
	
	
	
	@GetMapping(path = "findAll")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_LEVEL_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<UserLevelDTO>>> findAll(
			@RequestParam(value="level", required= false) String level,
			@PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.DESC) Pageable pageable) {
		
		GenericResponse<Page<UserLevelDTO>> res = this.userLevelWebService
				.findAll(level, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	@PostMapping(path="create")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_LEVEL_CREATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<UserLevelDTO>> create(
			@RequestBody UserLevelDTO toAdd) {
		
		GenericResponse<UserLevelDTO> res = this.userLevelWebService
				.addNewUserLevel(toAdd);
		
		return this.buildGenericResponse(res);
	}
	
	@PutMapping(path="update")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_LEVEL_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<UserLevelDTO>> update(
			@RequestBody UserLevelDTO toAdd) {
		
		GenericResponse<UserLevelDTO> res = this.userLevelWebService
				.updateUserLevel(toAdd);
		
		return this.buildGenericResponse(res);
	}
	
	
	@DeleteMapping(path="delete")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_LEVEL_DELETE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> delete(
			@RequestParam(value="levelId", required= true) Integer levelId) {
		
		GenericResponse<StringDTO> res = this.userLevelWebService.delete(levelId);
		
		return this.buildGenericResponse(res);
	}
	
	
	
}
