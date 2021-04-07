package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.tasks.ReportUserTaskDTO;
import it.palex.attendanceManagement.data.entities.core.ReportUserTask;


public class ReportUserTaskTransformer {

	public static ReportUserTaskDTO mapToDTO(ReportUserTask task) {
		if(task==null) {
			return null;
		}
		ReportUserTaskDTO res = new ReportUserTaskDTO();
		res.setDeleted(task.getDeleted());
		res.setId(task.getId());
		res.setLogs(task.getLogs());
		res.setMonth(task.getMonth());
		res.setStatus(task.getStatus());
		res.setYear(task.getYear());
		
		return res;
	}
	
	
	public static List<ReportUserTaskDTO> mapToDTO(List<ReportUserTask> tasks) {
		if(tasks==null) {
			return null;
		}
		
		List<ReportUserTaskDTO> res = new ArrayList<>(tasks.size());
		
		for (ReportUserTask reportUserTask : tasks) {
			res.add(mapToDTO(reportUserTask));
		}

		return res;
	}
	
	
	
}
