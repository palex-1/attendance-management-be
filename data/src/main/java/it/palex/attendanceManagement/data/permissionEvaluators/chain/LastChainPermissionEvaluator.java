package it.palex.attendanceManagement.data.permissionEvaluators.chain;

import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;

@Component
public class LastChainPermissionEvaluator implements ChainPermissionEvaluatorComponent{

	@Override
	public boolean hasPermission(UsersAuthDetails user, Object targetIdentifier, 
			String targetObjects, String permission) {
		return false;
	}

}
