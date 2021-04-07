package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.data.dto.tasks.SpecialTasksConfigDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.data.service.core.TasksUtilsService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping(path="specialTasks")
public class SpecialTaskUtilsController extends RestEndpoint {

	@Autowired
	private TasksUtilsService tasksUtilsService;
	
	@GetMapping(path = "/findAll")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<SpecialTasksConfigDTO>>> findAll() {
		
		GenericResponse<List<SpecialTasksConfigDTO>> res = this.tasksUtilsService.findSpecialTasksConfig();
		
		return this.buildGenericResponse(res);
	}
	
}
