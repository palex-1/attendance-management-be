package it.palex.attendanceManagement.data.repository.incarico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

import it.palex.attendanceManagement.data.entities.CompletedTask;
import it.palex.attendanceManagement.data.entities.QCompletedTask;
import it.palex.attendanceManagement.data.entities.QTeamComponentTask;
import it.palex.attendanceManagement.data.entities.QUserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.pseudoentities.WorkTaskSummaryPseudoEntity;
import it.palex.attendanceManagement.data.repository.generic.AbstractDAO;
import it.palex.attendanceManagement.data.utils.QueryDslUtils;

@Repository
public class CompletedTaskRepositoryCustomImpl extends AbstractDAO<CompletedTask> implements CompletedTaskRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public CompletedTaskRepositoryCustomImpl() {
		super(CompletedTask.class);
	}
	
	
	@Override
	public Pair<List<WorkTaskSummaryPseudoEntity>, Long> findWorkTaskSummary(WorkTask task, Date startDate, Date endDate, 
			String name, String surname, String email, Pageable pageable) {
		
		BooleanBuilder cond = new BooleanBuilder();
		//cond.and(QCompletedTask.completedTask.taskCode.isAbsenceTask.isFalse());
		cond.and(QCompletedTask.completedTask.taskCode.id.eq(task.getId()));
				
		if(startDate!=null) {
			cond.and(QCompletedTask.completedTask.day.goe(startDate));
		}
		if(endDate!=null) {
			cond.and(QCompletedTask.completedTask.day.loe(endDate));
		}
		
		//join table
		cond.and(QUserProfile.userProfile.id.eq(QCompletedTask.completedTask.userProfile.id));
		
		JPQLQuery<Long> sumOfWorkedHours = JPAExpressions.select(
				QCompletedTask.completedTask.workedHours.sum().castToNum(Long.class).coalesce(0l))
            .from(QCompletedTask.completedTask)
            .where(cond);

		JPQLQuery<Double> sumOfCost = JPAExpressions.select(
						QCompletedTask.completedTask.totalCost.sum().castToNum(Double.class).coalesce(0d))
				.from(QCompletedTask.completedTask)
				.where(cond);
		
		
		BooleanBuilder condQuery = new BooleanBuilder();
		
		
		if(name!=null) {
			condQuery.and(QUserProfile.userProfile.name.containsIgnoreCase(name));
		}
		if(surname!=null) {
			condQuery.and(QUserProfile.userProfile.surname.containsIgnoreCase(surname));
		}
		
		if(email!=null) {
			condQuery.and(QUserProfile.userProfile.email.containsIgnoreCase(email));
		}
		
		JPAQuery<Tuple> query = new JPAQuery<Tuple>(em);
		
		query.from(QUserProfile.userProfile);
		
		if(!BooleanUtils.isTrue(task.getIsEnabledForAllUser())) {
			condQuery.and(QTeamComponentTask.teamComponentTask.taskCode.id.eq(task.getId()));
			
			//make inner join to esclude component that are not part of the team
			query.innerJoin(QTeamComponentTask.teamComponentTask).on(
						QTeamComponentTask.teamComponentTask.userProfile.id.eq(QUserProfile.userProfile.id)
					);
		}
		
		
		//use a light count query
		query.select(QUserProfile.userProfile.id.as("userId"))
				.where(condQuery);
		
		long totalCount = query.fetchCount();
		
		
		query.select(QUserProfile.userProfile.id.as("userId"),
				QUserProfile.userProfile.name.as("name"), 
				QUserProfile.userProfile.surname.as("surname"),
				QUserProfile.userProfile.phoneNumber.as("phoneNumber"), 
				QUserProfile.userProfile.email.as("email"),
				QUserProfile.userProfile.company.id.as("companyId"), 
				QUserProfile.userProfile.company.name.as("companyName"),
				QUserProfile.userProfile.company.description.as("companyDescription"),
				Expressions.as(sumOfWorkedHours, "sumOfWorkedHours"),
				Expressions.as(sumOfCost, "sumOfCost"))
		.where(condQuery);
		
		
		
		
		query = query.offset(pageable.getPageNumber() * pageable.getPageSize());
		query = query.limit(pageable.getPageSize());

		
		ArrayList<OrderSpecifier> orderby = QueryDslUtils.sortToQueryDslOrderSpecifier(pageable.getSort());
		
		if(orderby != null && !orderby.isEmpty()) {
			OrderSpecifier[] park = new OrderSpecifier[orderby.size()];
			query.orderBy(orderby.toArray(park));
		}

		
		List<Tuple> resultSet = query.fetch();
		
		List<WorkTaskSummaryPseudoEntity> summary = new ArrayList<>(resultSet.size());

		for (Tuple entry : resultSet) {
			Object[] row = entry.toArray();
			WorkTaskSummaryPseudoEntity park = new WorkTaskSummaryPseudoEntity();
			
			park.setUserId(objectToInteger(row[0]));
			park.setUserName(objectToString(row[1]));
			park.setUserSurname(objectToString(row[2]));
			park.setUserPhoneNumber(objectToString(row[3]));
			park.setUserEmail(objectToString(row[4]));
			
			park.setCompanyId(objectToInteger(row[5]));
			park.setCompanyName(objectToString(row[6]));
			park.setCompanyDescription(objectToString(row[7]));
			
			park.setWorkedHours(bigDecimalToLong(row[8]));

			park.setSumOfCost(objectToDouble(row[9]));

			summary.add(park);
		}
		
		
		return Pair.of(summary, totalCount);
	}

	@Override
	public Double computeTotalHumanCost(WorkTask task, Date startDate, Date endDate) {
		BooleanBuilder cond = new BooleanBuilder();
		//cond.and(QCompletedTask.completedTask.taskCode.isAbsenceTask.isFalse());
		cond.and(QCompletedTask.completedTask.taskCode.id.eq(task.getId()));

		if(startDate!=null) {
			cond.and(QCompletedTask.completedTask.day.goe(startDate));
		}
		if(endDate!=null) {
			cond.and(QCompletedTask.completedTask.day.loe(endDate));
		}

		JPAQuery<Double> query = new JPAQuery<Double>(em);

		query.select(QCompletedTask.completedTask.totalCost.sum().castToNum(Double.class).coalesce(0d))
				.from(QCompletedTask.completedTask)
				.where(cond);

		List<Double> resultSet = query.fetch();

		if(resultSet.size()>1){
			throw new RuntimeException("Internal error resultSet.size()>1");
		}

		Double res = resultSet.get(0);

		return res;
	}


}
