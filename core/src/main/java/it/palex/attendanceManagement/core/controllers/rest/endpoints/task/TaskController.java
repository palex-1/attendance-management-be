package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

import com.querydsl.core.types.Predicate;

import it.palex.attendanceManagement.data.dto.tasks.WorkTaskDTO;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.data.service.incarico.WorkTaskService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * @author Alessandro Pagliaro
 *
 */
@RestController
@RequestMapping(path="task")
public class TaskController extends RestEndpoint {
	
	@Autowired
	private WorkTaskService workTaskService;
	
	@PostMapping
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.INCARICO_CREATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<WorkTaskDTO>> create(@RequestBody WorkTaskDTO task){
		GenericResponse<WorkTaskDTO> response = this.workTaskService.create(task);
			
		return this.buildGenericResponse(response);
	}
	
	@PutMapping
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.INCARICO_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<WorkTaskDTO>> update(@RequestBody WorkTaskDTO task){
		GenericResponse<WorkTaskDTO> response = this.workTaskService.update(task);
					
		return this.buildGenericResponse(response);
	}
	
	@PutMapping(path="deactivate/{taskCode}")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.INCARICO_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<WorkTaskDTO>> disattivaIncarico(
			@PathVariable("taskCode") String taskCode){
		GenericResponse<WorkTaskDTO> response = this.workTaskService.disattiva(taskCode);
					
		return this.buildGenericResponse(response);
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.INCARICO_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<WorkTaskDTO>>> findAll(
			@RequestParam(value="descrizioneIncaricoCUSTOMIZED", required=false) String descrizioneIncarico,
			  @RequestParam(value="codiceIncaricoCUSTOMIZED", required=false) String codiceIncarico,
				@PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.ASC) Pageable pageable,
            		@QuerydslPredicate(root = WorkTask.class) Predicate predicate){
		GenericResponse<Page<WorkTaskDTO>> response = this.workTaskService.findAll(pageable, predicate, descrizioneIncarico, codiceIncarico);
					
		return this.buildGenericResponse(response);
	}

	@DeleteMapping(path = "{taskCode}")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.INCARICO_DELETE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> delete(
			@PathVariable("taskCode") String taskCode){
		GenericResponse<StringDTO> response = this.workTaskService.delete(taskCode);
					
		return this.buildGenericResponse(response);
	}
	
	
}
