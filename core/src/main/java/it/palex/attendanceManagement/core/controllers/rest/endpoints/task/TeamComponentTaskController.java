package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.data.dto.impiegato.ImpiegatoSmallDTO;
import it.palex.attendanceManagement.data.dto.security.GrantedPermissionsDTO;
import it.palex.attendanceManagement.data.dto.tasks.ComponentCreationDTO;
import it.palex.attendanceManagement.data.dto.tasks.ComponenteTeamOutDTO;
import it.palex.attendanceManagement.data.dto.tasks.TeamIncaricoAddComponentsDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.data.permissionEvaluators.HasPermission;
import it.palex.attendanceManagement.data.service.incarico.TeamComponentTaskService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping(path="teamIncarico")
public class TeamComponentTaskController extends RestEndpoint {
	
	@Autowired
	private TeamComponentTaskService componenteTeamIncaricoSrv;
	
	@GetMapping("{taskId}")
    @HasPermission(targetObject = "TEAM_INCARICO", permission="READ", identifierParamName= "taskId")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<ComponenteTeamOutDTO>>> findAll(
				@PathVariable("taskId") Long taskId,
					@PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.ASC) Pageable pageable,
					@RequestParam(value="nomeImpiegatoCUSTOMIZED", required=false) String nomeImpiegato,
					@RequestParam(value="cognomeImpiegatoCUSTOMIZED", required=false) String cognomeImpiegato,
					@RequestParam(value="emailCUSTOMIZED", required=false) String emailImpiegato,
					@RequestParam(value="numeroTelefonoCUSTOMIZED", required=false) String numeroTelefonoImpiegato
					){
		GenericResponse<Page<ComponenteTeamOutDTO>> res = 
					this.componenteTeamIncaricoSrv.getAllImpegatiOfIncarico(taskId, 
							pageable, nomeImpiegato, cognomeImpiegato, emailImpiegato, numeroTelefonoImpiegato);
			
		return this.buildGenericResponse(res);
	}
	
	@GetMapping("allImpiegatiNotOf/{taskId}")
    @HasPermission(targetObject = "TEAM_INCARICO", permission="READ", identifierParamName= "taskId")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<ImpiegatoSmallDTO>>> findAllNotOf(
				@PathVariable("taskId") Long taskId,
					@PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.ASC) Pageable pageable,
					@RequestParam(value="nomeImpiegatoCUSTOMIZED", required=false) String nomeImpiegato,
					@RequestParam(value="cognomeImpiegatoCUSTOMIZED", required=false) String cognomeImpiegato,
					@RequestParam(value="emailCUSTOMIZED", required=false) String emailImpiegato,
					@RequestParam(value="numeroTelefonoCUSTOMIZED", required=false) String numeroTelefonoImpiegato
					){
		GenericResponse<Page<ImpiegatoSmallDTO>> res = 
					this.componenteTeamIncaricoSrv.getAllImpiegatiNotOfOfIncarico(taskId, 
							pageable, nomeImpiegato, cognomeImpiegato, emailImpiegato, numeroTelefonoImpiegato);
			
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping("permissionsGranted/{taskId}")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<GrantedPermissionsDTO>> getMyPermission(
			@PathVariable("taskId") Long taskId){
			GenericResponse<GrantedPermissionsDTO> res = 
					this.componenteTeamIncaricoSrv.getCurrentUserPermissions(taskId);
			
			return this.buildGenericResponse(res);
	}
	
	@PostMapping("simpleComponents/{taskId}")
    @HasPermission(targetObject = "TEAM_INCARICO", permission="UPDATE", identifierParamName= "taskId")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> addSimpleComponentiIncarico(
				@PathVariable("taskId") Long taskId,
				@RequestBody TeamIncaricoAddComponentsDTO componentsToAdd){
		GenericResponse<StringDTO> res = 
					this.componenteTeamIncaricoSrv.addImpiegatiWithGenericRoleToTeam(componentsToAdd, taskId);
			
		return this.buildGenericResponse(res);
	}
	
	@PutMapping("createSpecialComponent/{taskId}")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.TEAM_INCARICO_CREATE+"')") //do not use UPDATE permission. 
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> createSpecialComponent(
					@PathVariable("taskId") Long taskId,
						@RequestBody ComponentCreationDTO componentToCreate){
		GenericResponse<StringDTO> res = 
					this.componenteTeamIncaricoSrv.changeRoleInTeam(taskId, componentToCreate);
			
		return this.buildGenericResponse(res);
	}
	
	@DeleteMapping("deleteSpecialComponent/{taskId}")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.TEAM_INCARICO_DELETE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> deleteSpecialComponent(
			@PathVariable("taskId") Long taskId,
			ComponentCreationDTO componentToDelete){
		GenericResponse<StringDTO> res = 
					this.componenteTeamIncaricoSrv
					.removeSpecialImpiegatoFromTeam(taskId, componentToDelete);
			
		return this.buildGenericResponse(res);
	}
	
	
	/*
	 * SECURITY WARNING--> the @HasPermission cannot check that user can has permission to delete 
	 * specials components of team. Can check just if is a special user.
	 * A special user can only delete standard impiegati
	 */
	@DeleteMapping("deleteStandardComponent/{taskId}/{impiegatoId}") 
    @HasPermission(targetObject = "TEAM_INCARICO", permission="DELETE", identifierParamName= "taskId")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> deleteStandardComponent(
			@PathVariable("taskId") Long taskId,
				@PathVariable("impiegatoId") Integer impiegatoId){
		GenericResponse<StringDTO> res = 
					this.componenteTeamIncaricoSrv
						.removeStandardImpiegatoFromTeam(taskId, impiegatoId);
			
		return this.buildGenericResponse(res);
	}
	
}
