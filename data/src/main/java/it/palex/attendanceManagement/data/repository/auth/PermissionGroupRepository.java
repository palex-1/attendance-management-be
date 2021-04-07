package it.palex.attendanceManagement.data.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.PermissionGroup;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Integer>, 
		QuerydslPredicateExecutor<PermissionGroup> {
	
	
	
}
