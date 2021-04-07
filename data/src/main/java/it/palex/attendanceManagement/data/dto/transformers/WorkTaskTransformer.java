package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.tasks.IncaricoDetailsOutDTO;
import it.palex.attendanceManagement.data.dto.tasks.WorkTaskDTO;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;

/**
 * @author Alessandro Pagliaro
 *
 */
public class WorkTaskTransformer {


	public static List<WorkTaskDTO> mapToDTO(List<WorkTask> incarico) {
		if(incarico==null) {
			return null;
		}
		
		List<WorkTaskDTO> res = new ArrayList<>(incarico.size());
		
		for (WorkTask workTask : incarico) {
			res.add(mapToDTO(workTask));
		}

		return res;
	}
	
	public static WorkTaskDTO mapToDTO(WorkTask task) {
		if(task==null) {
			return null;
		}
		WorkTaskDTO out = new WorkTaskDTO();
		out.setId(task.getId());
		out.setTaskCode(task.getTaskCode());
		out.setActivationDate(task.getActivationDate());
		out.setDeactivationDate(task.getDeactivationDate());
		out.setTaskDescription(task.getTaskDescription());
		out.setBillable(task.getBillable());
		out.setClientVat(task.getClientVatNum());
		out.setIsEnabledForAllUsers(task.getIsEnabledForAllUser());
		out.setIsAbsenceTask(task.getIsAbsenceTask());
		
		return out;
	}
	
	public static IncaricoDetailsOutDTO mapToIncaricoDetailsOutDTO(WorkTask task,
			UserProfile projectManager, UserProfile deliveryManager, UserProfile accountManager,
				UserProfile qaReviewer) {
		IncaricoDetailsOutDTO out = new IncaricoDetailsOutDTO();
		if(task!=null) {
			out.setTask(WorkTaskTransformer.mapToDTO(task));
		}
		
		out.setProjectManager(ImpiegatoTransformer.mapToTinyDTO(projectManager));
		out.setDeliveryManager(ImpiegatoTransformer.mapToTinyDTO(deliveryManager));
		out.setAccountManager(ImpiegatoTransformer.mapToTinyDTO(accountManager));
		out.setQaReviewer(ImpiegatoTransformer.mapToTinyDTO(qaReviewer));
		
		return out;
	}
}
