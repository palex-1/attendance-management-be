package it.palex.attendanceManagement.data.repository.incarico;

import it.palex.attendanceManagement.data.entities.WorkTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.TaskExpenses;

import java.util.Date;


/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface TaskExpensesRepository extends JpaRepository<TaskExpenses, Long>, 
            QuerydslPredicateExecutor<TaskExpenses>, TaskExpensesRepositoryCustom {


}
