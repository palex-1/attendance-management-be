package it.palex.attendanceManagement.data.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.UsersAccessToken;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface UserAccessTokenRepository extends JpaRepository<UsersAccessToken, String>, 
				QuerydslPredicateExecutor<UsersAccessToken>, UserAccessTokenRepositoryCustom {

}
