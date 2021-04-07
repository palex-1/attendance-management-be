package it.palex.attendanceManagement.data.service.incarico;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.QTeamRole;
import it.palex.attendanceManagement.data.entities.TeamRole;
import it.palex.attendanceManagement.data.repository.incarico.TeamRoleRepository;


@Service
public class TeamRoleService {

	private final QTeamRole QTR = QTeamRole.teamRole;
	
	@Autowired
	private TeamRoleRepository teamRoleRepo;
	
	
	public TeamRole getByRuolo(String ruoloStr) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QTR.role.eq(ruoloStr));
		
		Iterator<TeamRole> it = this.teamRoleRepo.findAll(condition).iterator();
		
		if(it.hasNext()) {
			return it.next();
		}
		
		return null;
	}
	

}
