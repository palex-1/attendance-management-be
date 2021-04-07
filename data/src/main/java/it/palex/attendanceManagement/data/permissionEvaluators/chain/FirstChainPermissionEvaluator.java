package it.palex.attendanceManagement.data.permissionEvaluators.chain;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;

@Component
public class FirstChainPermissionEvaluator implements ChainPermissionEvaluatorComponent{

	@Autowired
	private TeamIncaricoChainPermissionEvaluator teamIncaricoCPE;
	
	@Override
	public boolean hasPermission(UsersAuthDetails user, Object targetIdentifier,
			String targetObjects, String permission) {
		return this.teamIncaricoCPE.hasPermission(user, targetIdentifier, 
				targetObjects, permission);
	}
	
}
