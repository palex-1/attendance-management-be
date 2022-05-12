package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import it.palex.attendanceManagement.core.dtos.tasks.TaskExpensesAddDTO;
import it.palex.attendanceManagement.core.dtos.tasks.TaskExpensesUpdateDTO;
import it.palex.attendanceManagement.core.service.tasks.TaskExpensesWebService;
import it.palex.attendanceManagement.data.dto.security.GrantedPermissionsDTO;
import it.palex.attendanceManagement.data.dto.settings.GlobalConfigurationsDTO;
import it.palex.attendanceManagement.data.dto.tasks.CompletedTaskDTO;
import it.palex.attendanceManagement.data.dto.tasks.TaskExpensesDTO;
import it.palex.attendanceManagement.data.dto.tasks.WorkTaskDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.data.permissionEvaluators.HasPermission;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.PairDTO;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path="task-expenses")
public class TaskExpensesController extends RestEndpoint {

    @Autowired
    private TaskExpensesWebService taskExpensesWebService;

    @GetMapping(path = "{taskId}")
    @HasPermission(targetObject = "TASK_EXPENSES", permission="READ", identifierParamName= "taskId")
    @Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<Page<TaskExpensesDTO>>> findAll(
            @PathVariable("taskId") Long taskId,
            @RequestParam(value="title", required=false) String title,
            @RequestParam(value="expenseType", required=false) String expenseType,
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @PageableDefault(page = 0, size = 10, sort={"id"},direction= Sort.Direction.ASC) Pageable pageable
            ) {

        Date dateStart = null;

        if(startDate!=null) {
            ZonedDateTime zonedDateTimeFrom = startDate.atZone(ZoneId.systemDefault());
            dateStart = Date.from(zonedDateTimeFrom.toInstant());
        }

        Date dateTo = null;

        if(endDate!=null) {
            ZonedDateTime zonedDateTimeTo = endDate.atZone(ZoneId.systemDefault());
            dateTo = Date.from(zonedDateTimeTo.toInstant());
        }

        GenericResponse<Page<TaskExpensesDTO>> res = this.taskExpensesWebService
                .findAll(taskId, title, expenseType, dateStart, dateTo, pageable);

        return this.buildGenericResponse(res);
    }

    @GetMapping(path = "permissionsGranted/{taskId}")
    @PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
    @Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<GrantedPermissionsDTO>> getUserExpensesPermissionsGranted(
            @PathVariable("taskId") Long taskId){
        GenericResponse<GrantedPermissionsDTO> response = this.taskExpensesWebService
                .getUserExpensesPermissionsGranted(taskId);

        return this.buildGenericResponse(response);
    }

    @GetMapping(path = "expensesTypes/findAll")
    @PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
    @Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<List<PairDTO<String, String>>>> getAllExpensesTypes(){
        GenericResponse<List<PairDTO<String, String>>> response = this.taskExpensesWebService
                .getAllExpensesTypes();

        return this.buildGenericResponse(response);
    }

    @PostMapping(path = "{taskId}")
    @HasPermission(targetObject = "TASK_EXPENSES", permission="CREATE", identifierParamName= "taskId")
    @Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<TaskExpensesDTO>> create(
            @PathVariable("taskId") Long taskId,
            @RequestBody TaskExpensesAddDTO task){
        GenericResponse<TaskExpensesDTO> response = this.taskExpensesWebService.create(taskId, task);

        return this.buildGenericResponse(response);
    }

    @PutMapping(path = "{taskId}")
    @HasPermission(targetObject = "TASK_EXPENSES", permission="UPDATE", identifierParamName= "taskId")
    @Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<TaskExpensesDTO>> update(
            @PathVariable("taskId") Long taskId,
            @RequestBody TaskExpensesUpdateDTO task){
        GenericResponse<TaskExpensesDTO> response = this.taskExpensesWebService.update(taskId, task);

        return this.buildGenericResponse(response);
    }


    @DeleteMapping(path = "{taskId}/{taskExpenseId}")
    @HasPermission(targetObject = "TASK_EXPENSES", permission="DELETE", identifierParamName= "taskId")
    @Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<StringDTO>> delete(
            @PathVariable("taskId") Long taskId,
            @PathVariable("taskExpenseId") Long taskExpenseId){
        GenericResponse<StringDTO> response = this.taskExpensesWebService.delete(taskId, taskExpenseId);

        return this.buildGenericResponse(response);
    }





}
