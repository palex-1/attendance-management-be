/**
 * 
 */
package it.palex.attendanceManagement.data.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.UserPasswordChangeHistory;
import it.palex.attendanceManagement.data.entities.auth.UserPasswordChangeHistoryPK;


/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface UserPasswordChangeHistoryRepository  extends JpaRepository<UserPasswordChangeHistory, UserPasswordChangeHistoryPK>, 
						QuerydslPredicateExecutor<UserPasswordChangeHistory>, CustomUserPasswordChangeHistoryRepository{

}
