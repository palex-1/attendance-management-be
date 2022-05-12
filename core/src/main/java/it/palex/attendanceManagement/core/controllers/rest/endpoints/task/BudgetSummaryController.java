package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import it.palex.attendanceManagement.auth.dto.BudgetSummaryGenerateRequest;
import it.palex.attendanceManagement.auth.dto.BudgetSummaryResponseDTO;
import it.palex.attendanceManagement.core.dtos.tasks.TaskExpensesAddDTO;
import it.palex.attendanceManagement.core.service.tasks.BudgetSummaryWebService;
import it.palex.attendanceManagement.data.dto.security.GrantedPermissionsDTO;
import it.palex.attendanceManagement.data.dto.tasks.TaskExpensesDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.data.permissionEvaluators.HasPermission;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="budget-summary")
public class BudgetSummaryController extends RestEndpoint {

    @Autowired
    private BudgetSummaryWebService budgetSummaryWebService;


    @PostMapping(path = "{taskId}")
    @HasPermission(targetObject = "BUDGET_SUMMARY", permission="CREATE", identifierParamName= "taskId")
    @Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<BudgetSummaryResponseDTO>> createBudgetSummary(
            @PathVariable("taskId") Long taskId,
            @RequestBody BudgetSummaryGenerateRequest request){

        GenericResponse<BudgetSummaryResponseDTO> response =
                this.budgetSummaryWebService.createBudgetSummary(taskId, request);

        return this.buildGenericResponse(response);
    }

    @GetMapping(path = "permissionsGranted/{taskId}")
    @PreAuthorize("hasAuthority('"+ AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
    @Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
    public ResponseEntity<GenericResponse<GrantedPermissionsDTO>> getUserBudgetSummaryPermissionsGranted(
            @PathVariable("taskId") Long taskId){

        GenericResponse<GrantedPermissionsDTO> response = this.budgetSummaryWebService
                .getUserBudgetSummaryPermissionsGranted(taskId);

        return this.buildGenericResponse(response);
    }

}
