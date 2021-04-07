package it.palex.attendanceManagement.data.service.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.Permissions;
import it.palex.attendanceManagement.data.entities.auth.Authorities;
import it.palex.attendanceManagement.data.entities.auth.QAuthorities;
import it.palex.attendanceManagement.data.repository.auth.AuthoritiesRepository;

@Service
public class AuthoritiesService {

	private final QAuthorities QA = QAuthorities.authorities;
	
	@Autowired
	private AuthoritiesRepository authoritiesRepo;

	public void saveAll(List<Authorities> userAuth) {
		if(userAuth==null) {
			throw new NullPointerException();
		}
		for (Authorities authority : userAuth) {
			this.authoritiesRepo.save(authority);
		}
	}

	public boolean checkIfUserHasPermission(Integer userId, Permissions perm) {
		if(perm==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QA.fkIdUsersAuthDetails.id.eq(userId));
		cond.and(QA.authority.authority.equalsIgnoreCase(perm.getAuthority()));
		
		return this.authoritiesRepo.count(cond) > 0;
		
	}
	
	
}

