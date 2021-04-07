package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.TaskCompletionsLocks;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 10 giu 2020
 */
@Repository
public interface TaskCompletionsLocksRepository extends JpaRepository<TaskCompletionsLocks, Integer>,
								QuerydslPredicateExecutor<TaskCompletionsLocks> {

}
