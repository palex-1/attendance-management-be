package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.ReportUserTask;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 3 lug 2020
 */
@Repository
public interface ReportUserTaskRepository  extends JpaRepository<ReportUserTask, Long>,
														QuerydslPredicateExecutor<ReportUserTask> {

	
	
	
}
