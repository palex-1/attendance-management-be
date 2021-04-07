package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.auth.PermissionGroupLabelDTO;
import it.palex.attendanceManagement.data.entities.auth.PermissionGroupLabel;

public class PermissionGroupLabelTransformer {

	
	public static PermissionGroupLabelDTO mapToDTO(PermissionGroupLabel label) {
		if(label==null) {
			return null;
		}
		
		PermissionGroupLabelDTO res = new PermissionGroupLabelDTO();
		res.setId(label.getId());
		res.setName(label.getName());
		
		return res;
	}
	
	public static List<PermissionGroupLabelDTO> mapToDTO(List<PermissionGroupLabel> list) {
		if(list==null) {
			return null;
		}
		
		List<PermissionGroupLabelDTO> res = new ArrayList<>();
		
		for (PermissionGroupLabel permissionGroupLabel : list) {
			res.add(mapToDTO(permissionGroupLabel));
		}
		
		return res;
	}
	
}
