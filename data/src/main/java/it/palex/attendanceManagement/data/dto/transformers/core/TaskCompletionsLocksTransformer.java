package it.palex.attendanceManagement.data.dto.transformers.core;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.tasks.TaskCompletionsLocksDTO;
import it.palex.attendanceManagement.data.entities.core.TaskCompletionsLocks;

public class TaskCompletionsLocksTransformer {

	public static TaskCompletionsLocksDTO mapToDTO(TaskCompletionsLocks lock) {
		if(lock==null) {
			return null;
		}
		TaskCompletionsLocksDTO dto = new TaskCompletionsLocksDTO();
		dto.setId(lock.getId());
		dto.setYear(lock.getYear());
		dto.setMonth(lock.getMonth());
		dto.setStatus(lock.getStatus());
		dto.setHoursCalculationExecutionRequested(lock.getHoursCalculationExecutionRequested());
		dto.setProcessedOnDate(lock.getProcessedOnDate());
		
		return dto;
	}
	
	
	public static List<TaskCompletionsLocksDTO> mapToDTO(List<TaskCompletionsLocks> locks){
		if(locks==null) {
			return null;
		}
		List<TaskCompletionsLocksDTO> res = new ArrayList<>(locks.size());
		
		for (TaskCompletionsLocks taskCompletionsLock : locks) {
			res.add(mapToDTO(taskCompletionsLock));
		}
		
		return res;
	}
	
	
	
	
}
