package it.palex.attendanceManagement.data.dto.transformers.core;

import it.palex.attendanceManagement.data.dto.core.WorkTransferRequestDTO;
import it.palex.attendanceManagement.data.entities.core.WorkTransferRequest;

public class WorkTransferRequestTransformer {

	public static WorkTransferRequestDTO mapToDTO(WorkTransferRequest entity) {
		if(entity==null) {
			return null;
		}
		WorkTransferRequestDTO res = new WorkTransferRequestDTO();
		res.setDay(entity.getDay());
		res.setId(entity.getId());
		res.setType(entity.getType());
		
		return res;
	}
	
}
