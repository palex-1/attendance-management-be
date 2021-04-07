package it.palex.attendanceManagement.data.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.SuccessfullyLoginLogs;
import it.palex.attendanceManagement.data.entities.auth.SuccessfullyLoginLogsPK;


/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface SuccessfullyLoginLogsRepository extends JpaRepository<SuccessfullyLoginLogs, SuccessfullyLoginLogsPK>, 
		QuerydslPredicateExecutor<SuccessfullyLoginLogs> , SuccessfullyLoginLogsRepositoryCustom {

}
