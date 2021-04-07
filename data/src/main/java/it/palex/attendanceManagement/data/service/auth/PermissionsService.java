package it.palex.attendanceManagement.data.service.auth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.Permissions;
import it.palex.attendanceManagement.data.entities.auth.PermissionGroup;
import it.palex.attendanceManagement.data.entities.auth.PermissionGroupLabel;
import it.palex.attendanceManagement.data.entities.auth.QPermissionGroup;
import it.palex.attendanceManagement.data.repository.auth.PermissionGroupRepository;


@Service
public class PermissionsService {

	private final QPermissionGroup QPG = QPermissionGroup.permissionGroup;

	@Autowired
	private PermissionGroupRepository permissionGroupRepo;
	
	
	
	public List<Permissions> findByGroup(PermissionGroupLabel groupLabel){
		if(groupLabel==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QPG.name.id.eq(groupLabel.getId()));
		
		List<Permissions> perms = new ArrayList<Permissions>();
		
		Iterator<PermissionGroup> it = this.permissionGroupRepo.findAll(cond).iterator();
		
		while(it.hasNext()) {
			perms.add(it.next().getFkAuthority());
		}
		
		return perms;
	}
	
	
	
	
}

