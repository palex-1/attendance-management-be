package it.palex.attendanceManagement.data.repository.failedLoginAttempt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.FailedLoginAttempt;
import it.palex.attendanceManagement.data.entities.auth.FailedLoginAttemptPK;


/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface FailedLoginAttemptRepository extends JpaRepository<FailedLoginAttempt, FailedLoginAttemptPK>, 
					QuerydslPredicateExecutor<FailedLoginAttempt>, 
					FailedLoginAttemptRepositoryCustom {

}
