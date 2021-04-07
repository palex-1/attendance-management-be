package it.palex.attendanceManagement.data.service.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.auth.PermissionGroupLabel;
import it.palex.attendanceManagement.data.entities.auth.QPermissionGroupLabel;
import it.palex.attendanceManagement.data.repository.auth.PermissionGroupLabelRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 30 mag 2020
 */
@Service
public class PermissionGroupLabelService implements BasicGenericService {

	private final QPermissionGroupLabel QPGL = QPermissionGroupLabel.permissionGroupLabel;
	
	@Autowired
	private PermissionGroupLabelRepository permissionGroupLabelRepository;
	
	
	
	public PermissionGroupLabel findById(Integer id) {
		if(id==null) {
			throw new NullPointerException();
		}
		Optional<PermissionGroupLabel> opt = this.permissionGroupLabelRepository.findById(id);
				
		return this.getFromOptional(opt);
	}
	
	
	
	public PermissionGroupLabel findByName(String name) {
		if(name==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QPGL.name.equalsIgnoreCase(name));
		
		return this.getFirstResultFromIterable(
					this.permissionGroupLabelRepository.findAll(cond)
				);
	}



	public List<PermissionGroupLabel> findAll(Sort sort) {
		return this.permissionGroupLabelRepository.findAll(sort);
	}
	
	
	
	
	
}
