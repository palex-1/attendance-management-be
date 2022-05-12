package it.palex.attendanceManagement.data.permissionEvaluators.chain;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.repository.incarico.TeamComponentTaskRepository;
import it.palex.attendanceManagement.data.service.incarico.TeamComponentTaskService;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BudgetSummaryPermissionEvaluator implements ChainPermissionEvaluatorComponent {

    public static final String PERMISSION_TO_CHECK = "BUDGET_SUMMARY";

    @Autowired
    private LastChainPermissionEvaluator nextInChain;

    @Autowired
    private TeamComponentTaskRepository teamComponentTaskRepository;


    @Override
    public boolean hasPermission(UsersAuthDetails user, Object targetIdentifier,
                                 String targetObjects, String permission) {
        if(!StringUtils.equals(PERMISSION_TO_CHECK, targetObjects)) {
            return this.nextInChain.hasPermission(user, targetIdentifier, targetObjects, permission);
        }

        //task identifier
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

        if(StringUtils.equals(ChainPermissions.UPDATE.name(), permission)) {
            if(this.hasUpdatePermissionOnTaskExpenses(auth.getUsername(), taskId)) {
                return true;
            }
        }

        if(StringUtils.equals(ChainPermissions.READ.name(), permission)) {
            if(this.hasReadPermissionOnTaskExpenses(auth.getUsername(), taskId)) {
                return true;
            }
        }

        if(StringUtils.equals(ChainPermissions.DELETE.name(), permission)) {
            if(this.hasDeletePermissionOnTaskExpenses(auth.getUsername(), taskId)) {
                return true;
            }
        }

        if(StringUtils.equals(ChainPermissions.CREATE.name(), permission)) {
            if(this.hasCreatePermissionOnTaskExpenses(auth.getUsername(), taskId)) {
                return true;
            }
        }

        return false; //no permission is granted to others
    }

    private boolean hasUpdatePermissionOnTaskExpenses(String username, Long taskId) {
        return teamComponentTaskRepository.isSpecialPartOfTheTeam(username, taskId);
    }

    private boolean hasReadPermissionOnTaskExpenses(String username, Long taskId) {
        return teamComponentTaskRepository.isSpecialPartOfTheTeam(username, taskId);
    }

    private boolean hasDeletePermissionOnTaskExpenses(String username, Long taskId) {
        return teamComponentTaskRepository.isSpecialPartOfTheTeam(username, taskId);
    }

    private boolean hasCreatePermissionOnTaskExpenses(String username, Long taskId) {
        return teamComponentTaskRepository.isSpecialPartOfTheTeam(username, taskId);
    }

}
