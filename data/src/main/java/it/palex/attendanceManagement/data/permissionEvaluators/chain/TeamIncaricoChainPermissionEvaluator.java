package it.palex.attendanceManagement.data.permissionEvaluators.chain;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.service.incarico.TeamComponentTaskService;

@Component
public class TeamIncaricoChainPermissionEvaluator implements ChainPermissionEvaluatorComponent {

	public static final String PERMISSION_TO_CHECK = "TEAM_INCARICO";
	
	@Autowired
	private WorkTaskDetailsChainPermissionEvaluator nextInChain; //move this after add a new ring i chain
	
	@Autowired
	private TeamComponentTaskService teamComponentTaskService;
	
	
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
			return true; //has admin permissions
		}		
		
		if(StringUtils.equals(ChainPermissions.READ.name(), permission)) {
			if(this.hasReadPermissionOnIncarico(auth.getUsername(), taskId)) {
				return true;
			}
		}
		
		if(StringUtils.equals(ChainPermissions.UPDATE.name(), permission)) {
			if(this.hasUpdatePermissionOnIncarico(auth.getUsername(), taskId)) {
				return true;
			}
		}
		
		if(StringUtils.equals(ChainPermissions.DELETE.name(), permission)) {
			if(this.hasDeletePermissionOnIncarico(auth.getUsername(), taskId)) {
				return true;
			}
		}
		
		return false; //no permission is granted to others
	}
	
	private boolean hasReadPermissionOnIncarico(String username, Long taskId) {
		return teamComponentTaskService.isPartOfTheTeam(username, taskId);
	}
	
	private boolean hasUpdatePermissionOnIncarico(String username, Long taskId) {
		return teamComponentTaskService.isSpecialPartOfTheTeam(username, taskId);
	}
	
	private boolean hasDeletePermissionOnIncarico(String username, Long taskId) {
		return teamComponentTaskService.isSpecialPartOfTheTeam(username, taskId);
	}
}
