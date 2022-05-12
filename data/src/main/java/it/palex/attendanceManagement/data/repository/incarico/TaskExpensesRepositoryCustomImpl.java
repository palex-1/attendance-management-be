package it.palex.attendanceManagement.data.repository.incarico;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import it.palex.attendanceManagement.data.entities.*;
import it.palex.attendanceManagement.data.entities.core.QTaskExpenses;
import it.palex.attendanceManagement.data.entities.core.TaskExpenses;
import it.palex.attendanceManagement.data.pseudoentities.WorkTaskSummaryPseudoEntity;
import it.palex.attendanceManagement.data.repository.generic.AbstractDAO;
import it.palex.attendanceManagement.data.utils.QueryDslUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class TaskExpensesRepositoryCustomImpl extends AbstractDAO<TaskExpenses> implements TaskExpensesRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public TaskExpensesRepositoryCustomImpl() {
		super(TaskExpenses.class);
	}
	
	@Override
	public Double computeTotalExpensesCost(WorkTask task, Date startDate, Date endDate) {
		BooleanBuilder cond = new BooleanBuilder();
		//cond.and(QCompletedTask.completedTask.taskCode.isAbsenceTask.isFalse());
		cond.and(QTaskExpenses.taskExpenses.workTask.id.eq(task.getId()));

		if(startDate!=null) {
			cond.and(QTaskExpenses.taskExpenses.day.goe(startDate));
		}
		if(endDate!=null) {
			cond.and(QTaskExpenses.taskExpenses.day.loe(endDate));
		}

		JPAQuery<Double> query = new JPAQuery<>(em);

		query.select(QTaskExpenses.taskExpenses.amount.sum().castToNum(Double.class).coalesce(0d))
				.from(QTaskExpenses.taskExpenses)
				.where(cond);

		List<Double> resultSet = query.fetch();

		if(resultSet.size()>1){
			throw new RuntimeException("Internal error resultSet.size()>1");
		}

		Double res =  resultSet.get(0);

		return res;
	}


}
