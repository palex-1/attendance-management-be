package it.palex.attendanceManagement.data.repository.incarico;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import it.palex.attendanceManagement.data.entities.QTeamComponentTask;
import it.palex.attendanceManagement.data.entities.TeamComponentTask;
import it.palex.attendanceManagement.data.entities.TeamRole;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.core.QTaskExpenses;
import it.palex.attendanceManagement.data.entities.core.TaskExpenses;
import it.palex.attendanceManagement.data.entities.enumTypes.TeamRoleEnum;
import it.palex.attendanceManagement.data.repository.generic.AbstractDAO;
import org.apache.commons.lang3.BooleanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
public class TeamComponentTaskRepositoryCustomImpl extends AbstractDAO<TeamComponentTask> implements TeamComponentTaskRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public TeamComponentTaskRepositoryCustomImpl() {
		super(TeamComponentTask.class);
	}

	@Override
	public boolean isSpecialPartOfTheTeam(String username, Long taskId) {
		if(username==null || taskId==null) {
			return false;
		}

		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QTeamComponentTask.teamComponentTask.taskCode.id.eq(taskId));
		condition.and(QTeamComponentTask.teamComponentTask.userProfile.usersAuthDetails.username.equalsIgnoreCase(username));
		condition.and(QTeamComponentTask.teamComponentTask.deleted.isFalse());

		JPAQuery<TeamComponentTask> query = new JPAQuery<>(em);

		query.select(QTeamComponentTask.teamComponentTask)
				.from(QTeamComponentTask.teamComponentTask)
				.where(condition);


		TeamComponentTask park = query.fetchFirst();

		if(park==null) {
			return false;
		}

		TeamRole ruoloUserProfile = park.getTeamRole();

		return this.isASpecialRole(ruoloUserProfile.getRole());
	}

	@Override
	public boolean isASpecialRole(String role) {
		if(StringUtils.equals(role, TeamRoleEnum.QA_REVIEWER.name()) ||
				StringUtils.equals(role, TeamRoleEnum.DELIVERY_MANAGER.name()) ||
				StringUtils.equals(role, TeamRoleEnum.PROJECT_MANAGER.name()) ||
				StringUtils.equals(role, TeamRoleEnum.ACCOUNT_MANAGER.name()) ) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isPartOfTheTeam(String username, Long taskId) {
		if(username==null || taskId==null) {
			return false;
		}

		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QTeamComponentTask.teamComponentTask.taskCode.id.eq(taskId));
		condition.and(QTeamComponentTask.teamComponentTask.userProfile.usersAuthDetails.username.equalsIgnoreCase(username));
		condition.and(QTeamComponentTask.teamComponentTask.deleted.isFalse());

		JPAQuery<Long> query = new JPAQuery<>(em);

		query.select(QTeamComponentTask.teamComponentTask.count())
				.from(QTeamComponentTask.teamComponentTask)
				.where(condition);


		Long count = query.fetchFirst();

		if(count>0){
			return true;
		}

		return false;
	}


}
