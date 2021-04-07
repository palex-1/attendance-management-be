package it.palex.attendanceManagement.data.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.UserContactType;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface UserContactTypeRepository  extends JpaRepository<UserContactType, Integer>, 
		QuerydslPredicateExecutor<UserContactType> {

}
