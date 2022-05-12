package it.palex.attendanceManagement.core.service.tasks;

import it.palex.attendanceManagement.core.dtos.tasks.TaskExpensesAddDTO;
import it.palex.attendanceManagement.core.dtos.tasks.TaskExpensesUpdateDTO;
import it.palex.attendanceManagement.data.dto.security.GrantedPermissionsDTO;
import it.palex.attendanceManagement.data.dto.settings.GlobalConfigurationsDTO;
import it.palex.attendanceManagement.data.dto.tasks.TaskExpensesDTO;
import it.palex.attendanceManagement.data.dto.tasks.WorkTaskDTO;
import it.palex.attendanceManagement.data.dto.transformers.TaskExpensesTransformer;
import it.palex.attendanceManagement.data.dto.transformers.WorkTaskTransformer;
import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.core.TaskExpenses;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.ChainPermissions;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.TaskExpensesPermissionEvaluator;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.TeamIncaricoChainPermissionEvaluator;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.incarico.TaskExpensesService;
import it.palex.attendanceManagement.data.service.incarico.WorkTaskService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.PairDTO;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class TaskExpensesWebService implements GenericService {

    @Autowired
    private TaskExpensesService taskExpensesService;

    @Autowired
    private WorkTaskService workTaskService;

    @Autowired
    private GlobalConfigurationsService globalConfigurationsService;

    @Autowired
    private TaskExpensesPermissionEvaluator taskExpensesPermissionEvaluator;

    @Autowired
    private CurrentAuthenticatedUserService currentAuthenticatedUserService;



    public GenericResponse<Page<TaskExpensesDTO>> findAll(Long taskId, String title, String expenseType,
                                                          Date startDate, Date endDate, Pageable pageable) {
        if(taskId==null){
            return this.buildBadDataResponse("Invalid Task id");
        }

        WorkTask task = this.workTaskService.findById(taskId);

        if(task==null){
            return this.buildNotFoundResponse("Task not found");
        }

        Pair<List<TaskExpenses>, Long> pair = this.taskExpensesService.findAllAndCount(title,
                                                                startDate, endDate, expenseType, task, pageable);

        List<TaskExpensesDTO> res = new ArrayList<>(pair.getKey().size());

        for(TaskExpenses expense: pair.getKey()) {
            res.add(TaskExpensesTransformer.mapToDTO(expense));
        }

        return this.buildPageableOkResponse(res, pair.getValue(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    public GenericResponse<StringDTO> delete(Long taskId, Long taskExpenseId) {
        if(taskExpenseId==null || taskId==null){
            return this.buildBadDataResponse();
        }

        WorkTask task = this.workTaskService.findById(taskId);

        if(task==null){
            return this.buildNotFoundResponse("Task not found");
        }

        TaskExpenses taskExpense = this.taskExpensesService.findById(taskExpenseId);

        if (taskExpense == null) {
            return this.buildNotFoundResponse("Cannot found Task expense with id specified");
        }

        // if the task trying to delete is not part of this task
        if(!taskExpense.getWorkTask().getId().equals(task.getId())){
            return this.buildBadDataResponse();
        }

        this.taskExpensesService.delete(taskExpense);

        return this.buildOkResponse(new StringDTO("Successully deleted"));
    }

    @Transactional(rollbackFor = Exception.class)
    public GenericResponse<TaskExpensesDTO> update(Long taskId, TaskExpensesUpdateDTO tasktoUpdate) {
        if(taskId==null || tasktoUpdate==null || tasktoUpdate.getId()==null
                || tasktoUpdate.getTitle()==null || tasktoUpdate.getExpenseType()==null
                || tasktoUpdate.getExpenseType()==null || tasktoUpdate.getDay()==null
                || tasktoUpdate.getAmount()==null){
            return this.buildBadDataResponse();
        }

        WorkTask task = this.workTaskService.findById(taskId);

        if(task==null){
            return this.buildNotFoundResponse("Task not found");
        }

        if(!isValidExpenseType(tasktoUpdate.getExpenseType())){
            return this.buildBadDataResponse(StandardReturnCodesEnum.EXPENSE_TYPE_NOT_VALID);
        }


        TaskExpenses entity = this.taskExpensesService.findById(tasktoUpdate.getId());

        if(entity==null){
            return this.buildNotFoundResponse();
        }

        // if the task trying to update is not part of this task
        if(!entity.getWorkTask().getId().equals(task.getId())){
            return this.buildBadDataResponse();
        }

        entity.setAmount(tasktoUpdate.getAmount());
        entity.setDescription(StringUtils.trim(tasktoUpdate.getDescription()));
        entity.setTitle(StringUtils.trim(tasktoUpdate.getTitle()));
        entity.setDay(tasktoUpdate.getDay());
        entity.setAmount(tasktoUpdate.getAmount());
        entity.setExpenseType(tasktoUpdate.getExpenseType());

        if (!entity.canBeInsertedInDatabase()) {
            return this.buildBadDataResponse("Data not valid");
        }

        entity = this.taskExpensesService.saveOrUpdate(entity);

        TaskExpensesDTO out = TaskExpensesTransformer.mapToDTO(entity);

        return this.buildOkResponse(out);
    }

    @Transactional(rollbackFor = Exception.class)
    public GenericResponse<TaskExpensesDTO> create(Long taskId, TaskExpensesAddDTO taskToAdd) {
        if(taskId==null || taskToAdd==null || taskToAdd.getTitle()==null || taskToAdd.getExpenseType()==null
                || taskToAdd.getExpenseType()==null || taskToAdd.getDay()==null || taskToAdd.getAmount()==null){
            return this.buildBadDataResponse();
        }

        WorkTask task = this.workTaskService.findById(taskId);

        if(task==null){
            return this.buildNotFoundResponse("Task not found");
        }

        if(!isValidExpenseType(taskToAdd.getExpenseType())){
            return this.buildBadDataResponse(StandardReturnCodesEnum.EXPENSE_TYPE_NOT_VALID);
        }

        TaskExpenses entity = new TaskExpenses();
        entity.setAmount(taskToAdd.getAmount());
        entity.setDescription(StringUtils.trim(taskToAdd.getDescription()));
        entity.setTitle(StringUtils.trim(taskToAdd.getTitle()));
        entity.setDay(taskToAdd.getDay());
        entity.setAmount(taskToAdd.getAmount());
        entity.setExpenseType(taskToAdd.getExpenseType());
        entity.setWorkTask(task);


        if (!entity.canBeInsertedInDatabase()) {
            return this.buildBadDataResponse("Data not valid");
        }

        entity = this.taskExpensesService.saveOrUpdate(entity);

        TaskExpensesDTO out = TaskExpensesTransformer.mapToDTO(entity);

        return this.buildOkResponse(out);
    }


    private boolean isValidExpenseType(String expenseType){
        if(expenseType==null){
            return false;
        }

        if(!TaskExpenses.isValidExpenseType(expenseType)){
            return false;
        }

        List<GlobalConfigurations> allConfigs = this.globalConfigurationsService.findAllByArea(
                                            GlobalConfigurationSettingsTuple.TASK_EXPENSE_TYPES.AREA_NAME
                                    );

        for (GlobalConfigurations config:
                allConfigs) {
            if(expenseType.equals(config.getSettingKey())){
                return true;
            }
        }

        return false;
    }


    public GenericResponse<GrantedPermissionsDTO> getUserExpensesPermissionsGranted(Long taskId) {
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
                this.taskExpensesPermissionEvaluator.checkPermissionWithoutChain(auth, taskId,
                        TaskExpensesPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.READ.name());
        Boolean creationPermission =
                this.taskExpensesPermissionEvaluator.checkPermissionWithoutChain(auth, taskId,
                        TaskExpensesPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.CREATE.name());
        Boolean updatePermission =
                this.taskExpensesPermissionEvaluator.checkPermissionWithoutChain(auth, taskId,
                        TaskExpensesPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.UPDATE.name());
        Boolean deletePermission =
                this.taskExpensesPermissionEvaluator.checkPermissionWithoutChain(auth, taskId,
                        TaskExpensesPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.DELETE.name());

        GrantedPermissionsDTO perm = new GrantedPermissionsDTO();
        perm.setCreationPermission(creationPermission);
        perm.setDeletePermission(deletePermission);
        perm.setReadPermission(readPermission);
        perm.setUpdatePermission(updatePermission);

        return this.buildOkResponse(perm);
    }

    public GenericResponse<List<PairDTO<String, String>>> getAllExpensesTypes() {
        List<GlobalConfigurations> allConfigs = this.globalConfigurationsService.findAllByArea(
                GlobalConfigurationSettingsTuple.TASK_EXPENSE_TYPES.AREA_NAME
        );

        ArrayList<PairDTO<String, String>> res = new ArrayList<>(allConfigs.size());

        for (GlobalConfigurations config:
                allConfigs) {
            res.add(new PairDTO<String, String>(config.getSettingKey(), config.getSettingValue()));
        }

        return this.buildOkResponse(res);
    }


}
