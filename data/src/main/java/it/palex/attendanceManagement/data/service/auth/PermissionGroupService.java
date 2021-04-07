package it.palex.attendanceManagement.data.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.data.entities.auth.PermissionGroup;
import it.palex.attendanceManagement.data.entities.auth.QPermissionGroup;
import it.palex.attendanceManagement.data.repository.auth.PermissionGroupRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class PermissionGroupService implements BasicGenericService {

	private final QPermissionGroup QPG = QPermissionGroup.permissionGroup;
	
	@Autowired
	private PermissionGroupRepository permissionGroupRepo;
	
	
	public PermissionGroup findById(Integer id) {
		if(id==null) {
			throw new NullPointerException();
		}
		
		return this.getFromOptional(this.permissionGroupRepo.findById(id));
	}
	
	
}
