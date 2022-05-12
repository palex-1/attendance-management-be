package it.palex.attendanceManagement.data.permissionEvaluators.chain;

import it.palex.attendanceManagement.data.repository.incarico.TeamComponentTaskRepository;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.service.incarico.TeamComponentTaskService;

@Component
public class WorkTaskDetailsChainPermissionEvaluator  implements ChainPermissionEvaluatorComponent {

	public static final String PERMISSION_TO_CHECK = "TASK_DETAILS";
	
	@Autowired
	private TaskExpensesPermissionEvaluator nextInChain; //move this after add a new ring i chain

	@Autowired
	private TeamComponentTaskRepository teamComponentTaskRepository;
	
	
	@Override
	public boolean hasPermission(UsersAuthDetails user, Object targetIdentifier,
			String targetObjects, String permission) {
		if(!StringUtils.equals(PERMISSION_TO_CHECK, targetObjects)) {
			return this.nextInChain.hasPermission(user, targetIdentifier, targetObjects, permission);
		}
		//the identifier of a team is a 
		Long taskId = (Long) targetIdentifier;
		
		return this.checkPermissionWithoutChain(user, taskId, targetObjects, permission);
	}
	
	
	public boolean checkPermissionWithoutChain(UsersAuthDetails auth, Long taskId,
			String targetObjects, String permission) {
		String authority = targetObjects+"_"+permission;
		
		if(auth==null) {
			return false;
		}
		
		if(AuthoritiesChecker.hasAuthority(auth.getAuthorities(), authority)) {
			return true; //has authority permissions
		}	
		
		if(StringUtils.equals(ChainPermissions.READ.name(), permission)) {
			if(this.hasUpdatePermissionOnIncarico(auth.getUsername(), taskId)) {
				return true;
			}
		}
		
		return false; //no permission is granted to others
	}
	
	private boolean hasUpdatePermissionOnIncarico(String username, Long taskId) {
		return teamComponentTaskRepository.isSpecialPartOfTheTeam(username, taskId);
	}
	
}
