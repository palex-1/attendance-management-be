package it.palex.attendanceManagement.data.repository.incarico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.WorkTask;

/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface WorkTaskRepository extends JpaRepository<WorkTask, Long>, 
												QuerydslPredicateExecutor<WorkTask>{

}
