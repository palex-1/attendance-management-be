package it.palex.attendanceManagement.data.permissionEvaluators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.FirstChainPermissionEvaluator;
import it.palex.attendanceManagement.data.repository.auth.UsersAuthDetailsRepository;

@Service
public class ChainPermissionEvaluator {
	
	@Autowired
	private FirstChainPermissionEvaluator firstChainEvaluator;
	
	@Autowired
	private UsersAuthDetailsRepository userAuthDetailsRepo;
	
	boolean hasPermission(Authentication auth, Object targetDomainObject, 
			String targetObjects, String permission) {
		if(auth==null){
			return false;
		}
		UsersAuthDetails user = getUserIdByHashedUsername(auth.getName());
		if(user==null) {
			return false;
		}
		
		return this.firstChainEvaluator.hasPermission(user, 
				targetDomainObject, targetObjects, permission);
	}
	
	public UsersAuthDetails getUserIdByHashedUsername(String hashedUsername) {
		return this.userAuthDetailsRepo.findByHashedUsername(hashedUsername);
	}
	
}