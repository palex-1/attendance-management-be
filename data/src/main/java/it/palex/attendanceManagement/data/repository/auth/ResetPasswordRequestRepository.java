package it.palex.attendanceManagement.data.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.ResetPasswordRequest;
import it.palex.attendanceManagement.data.entities.auth.ResetPasswordRequestPK;


/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface ResetPasswordRequestRepository extends JpaRepository<ResetPasswordRequest, ResetPasswordRequestPK>, 
	QuerydslPredicateExecutor<ResetPasswordRequest> , ResetPasswordRequestRepositoryCustom{

	
}
