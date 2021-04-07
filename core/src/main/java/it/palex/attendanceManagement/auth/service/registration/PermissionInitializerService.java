package it.palex.attendanceManagement.auth.service.registration;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.entities.Permissions;
import it.palex.attendanceManagement.data.entities.auth.Authorities;
import it.palex.attendanceManagement.data.entities.auth.PermissionGroup;
import it.palex.attendanceManagement.data.entities.auth.PermissionGroupLabel;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.service.auth.AuthoritiesService;
import it.palex.attendanceManagement.data.service.auth.PermissionsService;

@Component
public class PermissionInitializerService {

	@Autowired
	private PermissionsService permissionSrv;

	@Autowired
	private AuthoritiesService authoritiesSrv;
	
	
	public void addPermissionToUserByPermissionGroup(UsersAuthDetails newUser, PermissionGroupLabel group) {
		if (newUser == null || group == null) {
			throw new NullPointerException();
		}
		List<Permissions> res = this.permissionSrv.findByGroup(group);

		List<Authorities> userAuth = newUser.getAuthoritiesList();
		
		if(userAuth==null) {
			userAuth = new LinkedList<Authorities>();
		}
		

		for (Permissions perm : res) {
			
			boolean userHasPermission =  this.authoritiesSrv.checkIfUserHasPermission(newUser.getId(), perm);
			
			if(!userHasPermission) {
				Authorities auth = new Authorities();
				auth.setFkIdUsersAuthDetails(newUser);
				auth.setAuthority(perm);
				userAuth.add(auth);
			}
			
		}

		this.authoritiesSrv.saveAll(userAuth);
	}
	
}
