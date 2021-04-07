package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.UserLevel;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 5 giu 2020
 */
@Repository
public interface UserLevelRepository extends JpaRepository<UserLevel, Integer>,
				QuerydslPredicateExecutor<UserLevel> {

}
