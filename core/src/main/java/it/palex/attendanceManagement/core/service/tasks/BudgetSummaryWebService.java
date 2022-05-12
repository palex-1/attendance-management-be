package it.palex.attendanceManagement.core.service.tasks;

import it.palex.attendanceManagement.auth.dto.BudgetSummaryGenerateRequest;
import it.palex.attendanceManagement.auth.dto.BudgetSummaryResponseDTO;
import it.palex.attendanceManagement.auth.dto.TemporalRangeDTO;
import it.palex.attendanceManagement.core.dtos.tasks.WorkTaskSummaryDTO;
import it.palex.attendanceManagement.data.dto.core.CompanyDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileSmallDTO;
import it.palex.attendanceManagement.data.dto.security.GrantedPermissionsDTO;
import it.palex.attendanceManagement.data.dto.tasks.CompletedTaskDTO;
import it.palex.attendanceManagement.data.dto.transformers.CompletedTaskTransformer;
import it.palex.attendanceManagement.data.entities.CompletedTask;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.BudgetSummaryPermissionEvaluator;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.ChainPermissions;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.TaskExpensesPermissionEvaluator;
import it.palex.attendanceManagement.data.pseudoentities.WorkTaskSummaryPseudoEntity;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.incarico.CompletedTaskService;
import it.palex.attendanceManagement.data.service.incarico.TaskExpensesService;
import it.palex.attendanceManagement.data.service.incarico.TaskSummaryService;
import it.palex.attendanceManagement.data.service.incarico.WorkTaskService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BudgetSummaryWebService implements GenericService {

	private static final int MAX_BINS_TO_DIVIDE_RANGE = 20;

	@Autowired
	private WorkTaskService workTaskService;

	@Autowired
	private TaskExpensesService taskExpensesService;
	
	@Autowired
	private CompletedTaskService completedTaskService;
	
	@Autowired
	private TaskSummaryService taskSummaryService;

	@Autowired
	private BudgetSummaryPermissionEvaluator budgetSummaryPermissionEvaluator;

	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;


	public GenericResponse<BudgetSummaryResponseDTO> createBudgetSummary(Long taskId, BudgetSummaryGenerateRequest request) {
		if(taskId==null || request.getDateFrom()==null || request.getDateTo()==null || request.getAddExpensesReport()==null
				|| request.getBinsForTheRange()==null || request.getAddHumanExpensesReport()==null){
			return this.buildBadDataResponse();
		}

		if(request.getBinsForTheRange().intValue()<1 || request.getBinsForTheRange().intValue()>MAX_BINS_TO_DIVIDE_RANGE) {
			return this.buildBadDataResponse("Bins for the range must be <" + MAX_BINS_TO_DIVIDE_RANGE + " and >=1");
		}

		WorkTask task = this.workTaskService.findById(taskId);
		if(task==null) {
			return this.buildNotFoundResponse();
		}

		Date startPark = DateUtility.startOfDayOfDate(request.getDateFrom());
		Date endPark = DateUtility.endOfDayOfDate(request.getDateTo());

		if(startPark.after(endPark)){
			return this.buildBadDataResponse(StandardReturnCodesEnum.DATE_START_AFTER_DATE_END);
		}

		BudgetSummaryResponseDTO res = new BudgetSummaryResponseDTO();
		res.setTotalBudget(task.getTotalBudget());

		Date currentDate = DateUtility.getCurrentDateInUTC();
		if(currentDate.after(task.getActivationDate())){
			long days = DateUtility.diffInDays(currentDate, task.getActivationDate());
			res.setTaskCreationDays(days);
		}else{
			res.setTaskCreationDays(0l);
		}

		List<Double> humanCostSummary = new ArrayList<>();
		List<Double> expensesCostSummary = new ArrayList<>();
		List<TemporalRangeDTO> temporalRangeBinSummary = new ArrayList<>();


		long totalRangeInSec = DateUtility.diffInSeconds(endPark, startPark);
		long rangeSecondsBins = totalRangeInSec / request.getBinsForTheRange().intValue();

		//compute the cost until this the startDate
		if(request.getAddHumanExpensesReport()){
			Double totalHumanCost = this.taskSummaryService.computeTotalHumanCost(task, null, startPark);
			humanCostSummary.add(totalHumanCost);
		}

		if(request.getAddExpensesReport()){
			Double totalExpensesCost = this.taskExpensesService.computeTotalExpensesCost(task, null, startPark);
			expensesCostSummary.add(totalExpensesCost);
		}

		TemporalRangeDTO startTempRangeDTO = new TemporalRangeDTO();
		startTempRangeDTO.setDateStart(null);
		startTempRangeDTO.setDateEnd(startPark);

		temporalRangeBinSummary.add(startTempRangeDTO);

		//add the other
		Date startDate = startPark;


		for(int i=0; i<request.getBinsForTheRange().intValue(); i++){
			long secondsToAdd = i * rangeSecondsBins;

			Date endDate = DateUtility.addSecondsToDate(startDate, (int)secondsToAdd);

			if(request.getAddHumanExpensesReport()){
				Double totalHumanCost = this.taskSummaryService.computeTotalHumanCost(task, startDate, endDate);
				humanCostSummary.add(totalHumanCost);
			}

			if(request.getAddExpensesReport()){
				Double totalExpensesCost = this.taskExpensesService.computeTotalExpensesCost(task, startDate, endDate);
				expensesCostSummary.add(totalExpensesCost);
			}

			TemporalRangeDTO parkTempDTO = new TemporalRangeDTO();
			parkTempDTO.setDateStart(startDate);
			parkTempDTO.setDateEnd(endDate);

			temporalRangeBinSummary.add(parkTempDTO);

			startDate = endDate;
		}

		res.setExpensesCostSummary(expensesCostSummary);
		res.setHumanCostSummary(humanCostSummary);
		res.setTemporalRangeBinSummary(temporalRangeBinSummary);

		return this.buildOkResponse(res);
	}

    public GenericResponse<GrantedPermissionsDTO> getUserBudgetSummaryPermissionsGranted(Long taskId) {
		if(taskId==null){
			return this.buildBadDataResponse();
		}
		WorkTask task = this.workTaskService.findById(taskId);
		if(task==null) {
			return this.buildNotFoundResponse();
		}

		UsersAuthDetails auth = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserAuthDetails();
		if(auth==null) {
			return null;
		}
		Boolean readPermission =
				this.budgetSummaryPermissionEvaluator.checkPermissionWithoutChain(auth, taskId,
						BudgetSummaryPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.READ.name());
		Boolean creationPermission =
				this.budgetSummaryPermissionEvaluator.checkPermissionWithoutChain(auth, taskId,
						BudgetSummaryPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.CREATE.name());
		Boolean updatePermission =
				this.budgetSummaryPermissionEvaluator.checkPermissionWithoutChain(auth, taskId,
						BudgetSummaryPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.UPDATE.name());
		Boolean deletePermission =
				this.budgetSummaryPermissionEvaluator.checkPermissionWithoutChain(auth, taskId,
						BudgetSummaryPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.DELETE.name());

		GrantedPermissionsDTO perm = new GrantedPermissionsDTO();
		perm.setCreationPermission(creationPermission);
		perm.setDeletePermission(deletePermission);
		perm.setReadPermission(readPermission);
		perm.setUpdatePermission(updatePermission);

		return this.buildOkResponse(perm);
    }



}
