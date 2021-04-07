package it.palex.attendanceManagement.data.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.PermissionGroupLabel;


/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 30 mag 2020
 */
@Repository
public interface PermissionGroupLabelRepository extends JpaRepository<PermissionGroupLabel, Integer>, 
			QuerydslPredicateExecutor<PermissionGroupLabel> {
	

}
