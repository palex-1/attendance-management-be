package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.tasks.CompletedTaskDTO;
import it.palex.attendanceManagement.data.entities.CompletedTask;

public class CompletedTaskTransformer {

	public static CompletedTaskDTO mapToDTO(CompletedTask task, boolean includeCosts) {
		if(task==null) {
			return null;
		}
		CompletedTaskDTO res = new CompletedTaskDTO();
		res.setDay(task.getDay());
		res.setEditable(task.getEditable());
		res.setId(task.getId());
		res.setSmartworked(task.getSmartworked());
		res.setTaskCode(WorkTaskTransformer.mapToDTO(task.getTaskCode(), includeCosts));
		res.setUserProfile(UserProfileTransformer.mapToTinyDTO(task.getUserProfile()));
		res.setWorkedHours(task.getWorkedHours());
		res.setActivityDescription(task.getActivityDescription());
		
		if(includeCosts) {
			res.setTotalCost(task.getTotalCost());
		}
				
		return res;
	}
	
	public static List<CompletedTaskDTO> mapToDTO(List<CompletedTask> tasks, boolean includeCosts) {
		if(tasks==null) {
			return null;
		}
		
		List<CompletedTaskDTO> res = new ArrayList<>(tasks.size());
		
		for (CompletedTask completedTask : tasks) {
			res.add(mapToDTO(completedTask, includeCosts));
		}
		
		return res;
	}

}
