package it.palex.attendanceManagement.data.service.user;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.QUserContactType;
import it.palex.attendanceManagement.data.entities.UserContactType;
import it.palex.attendanceManagement.data.repository.auth.UserContactTypeRepository;

@Service
public class UserContactTypeService {

	private final QUserContactType QUCT = QUserContactType.userContactType;
	
	@Autowired
	private UserContactTypeRepository userContactTypeRepo;
	
	
	public UserContactType findByKey(String type) {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUCT.cType.equalsIgnoreCase(type));
		
		Iterator<UserContactType> it = this.userContactTypeRepo.findAll(cond).iterator();
		
		if(it.hasNext()) {
			return it.next();
		}
		
		return null;
	}
	
	
}
