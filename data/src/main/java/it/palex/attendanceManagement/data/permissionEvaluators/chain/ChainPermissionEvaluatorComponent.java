package it.palex.attendanceManagement.data.permissionEvaluators.chain;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;

public interface ChainPermissionEvaluatorComponent {

	boolean hasPermission(UsersAuthDetails user, Object targetIdentifier, 
			String targetObjects,String permission);
}
