package it.palex.attendanceManagement.core.service.tasks;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.core.dtos.tasks.CompletionLockAddRequest;
import it.palex.attendanceManagement.data.dto.tasks.TaskCompletionsLocksDTO;
import it.palex.attendanceManagement.data.dto.transformers.core.TaskCompletionsLocksTransformer;
import it.palex.attendanceManagement.data.entities.core.TaskCompletionsLocks;
import it.palex.attendanceManagement.data.entities.enumTypes.TaskCompletionLocksStatusEnum;
import it.palex.attendanceManagement.data.service.core.TaskCompletionsLocksService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;

@Service
public class TaskCompletionLockWebService implements GenericService {

	@Autowired
	private TaskCompletionsLocksService taskCompletionsLocksService;
	
	
	public GenericResponse<Page<TaskCompletionsLocksDTO>> findAll(Integer month, Integer year, String status, Pageable pageable){
		if(pageable==null) {
			return this.buildBadDataResponse();
		}
		
		Pair<List<TaskCompletionsLocks>, Long> pair = this.taskCompletionsLocksService.findAllAndCount(month, year, status, pageable);
		
		long totalCount = pair.getValue();
		List<TaskCompletionsLocksDTO> list = TaskCompletionsLocksTransformer.mapToDTO(pair.getKey());
		
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<TaskCompletionsLocksDTO> addNewCompletionLock(CompletionLockAddRequest lock){
		if(lock==null || lock.getHoursCalculationExecutionRequested()==null || lock.getMonth()==null || lock.getYear()==null) {
			return this.buildBadDataResponse();
		}
		
		TaskCompletionsLocks oldLock = this.taskCompletionsLocksService.findByYearAndMonth(lock.getYear(), lock.getMonth());
		
		if(oldLock!=null) {
			return this.buildConflictEntity(StandardReturnCodesEnum.ALREADY_EXISTS_A_LOCK_FOR_THIS_YEAR_AND_MONTH);
		}
	
		TaskCompletionsLocks toCreate = new TaskCompletionsLocks();
		toCreate.setHoursCalculationExecutionRequested(lock.getHoursCalculationExecutionRequested());
		toCreate.setYear(lock.getYear());
		toCreate.setMonth(lock.getMonth());
		toCreate.setProcessedOnDate(null);
		
		if(!lock.getHoursCalculationExecutionRequested()) {
			toCreate.setStatus(TaskCompletionLocksStatusEnum.NOT_TO_BE_PROCESSED.name());
		}else {
			toCreate.setStatus(TaskCompletionLocksStatusEnum.TO_BE_PROCESSED.name());
		}
		
		toCreate = this.taskCompletionsLocksService.saveOrUpdate(toCreate);
		
		return this.buildOkResponse(TaskCompletionsLocksTransformer.mapToDTO(toCreate));
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<TaskCompletionsLocksDTO> requestHoursCalculationExecution(Integer taskCompletionLockId){
		if(taskCompletionLockId==null) {
			return this.buildBadDataResponse();
		}
		
		TaskCompletionsLocks lock = this.taskCompletionsLocksService.findById(taskCompletionLockId);
		
		if(lock==null) {
			return this.buildNotFoundResponse();
		}
		
		//if the calculation was not requested
		if(!lock.getHoursCalculationExecutionRequested()) {
			lock.setStatus(TaskCompletionLocksStatusEnum.TO_BE_PROCESSED.name());
			lock.setHoursCalculationExecutionRequested(true);
		}
		
		lock = this.taskCompletionsLocksService.saveOrUpdate(lock);
		
		return this.buildOkResponse(TaskCompletionsLocksTransformer.mapToDTO(lock));
	}
	
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> delete(Integer taskCompletionLockId){
		if(taskCompletionLockId==null) {
			return this.buildBadDataResponse();
		}
		
		TaskCompletionsLocks lock = this.taskCompletionsLocksService.findById(taskCompletionLockId);
		
		if(lock==null) {
			return this.buildNotFoundResponse();
		}
		
		//if the status is processed or processing the lock cannot be deleted
		if(StringUtils.equals(lock.getStatus(), TaskCompletionLocksStatusEnum.PROCESSING.name()) 
				|| StringUtils.equals(lock.getStatus(), TaskCompletionLocksStatusEnum.PROCESSED.name())) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.LOCK_CANNOT_BE_DELETED_ANYMORE);
		}
		
		this.taskCompletionsLocksService.delete(lock);
		
		return this.buildStringOkResponse("Successfully deleted");
	}
	
	
	
	
	
	
}
