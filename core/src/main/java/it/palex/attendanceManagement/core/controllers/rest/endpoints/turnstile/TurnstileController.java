package it.palex.attendanceManagement.core.controllers.rest.endpoints.turnstile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.dtos.turnstile.TurnstileAddRequest;
import it.palex.attendanceManagement.core.dtos.turnstile.TurnstileUpdateRequest;
import it.palex.attendanceManagement.core.service.turnstile.TurnstileWebService;
import it.palex.attendanceManagement.data.dto.core.TurnstileDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;


@RestController
@RequestMapping(path="turnstile")
public class TurnstileController extends RestEndpoint {

	@Autowired
	private TurnstileWebService turnstileWebService;
	
	
	@GetMapping(path = "findAll")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.TURNSTILE_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<TurnstileDTO>>> findAll(
			@RequestParam(value="title", required= false) String title,
			@RequestParam(value="position", required= false) String position,
			@RequestParam(value="description", required= false) String description,
			@RequestParam(value="includeDisabled", required= true, defaultValue= "true") Boolean includeDisabled,
			@PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.DESC) Pageable pageable) {
		
		GenericResponse<Page<TurnstileDTO>> res = this.turnstileWebService
				.findAllTurnstile(title, position, description, includeDisabled, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path = "findDetails")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.TURNSTILE_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TurnstileDTO>> findAll(
			@RequestParam(value="turnstileId", required= true) Long turnstileId) {
		GenericResponse<TurnstileDTO> res = this.turnstileWebService
				.findTurnstileDetails(turnstileId);
		
		return this.buildGenericResponse(res);
	}
	
	
	@PostMapping(path="create")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.TURNSTILE_CREATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TurnstileDTO>> create(
			@RequestBody TurnstileAddRequest addRequest){
		
		GenericResponse<TurnstileDTO> response = this.turnstileWebService
				.addNewTurnstile(addRequest);
					
		return this.buildGenericResponse(response);
	}
	
	
	@PutMapping(path="update")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.TURNSTILE_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TurnstileDTO>> disableTurnstile(
			@RequestBody TurnstileUpdateRequest turnstile){
		
		GenericResponse<TurnstileDTO> response = this.turnstileWebService
				.updateTurnstile(turnstile);
					
		return this.buildGenericResponse(response);
	}
	
	
	
	
	
}
