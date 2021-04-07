package it.palex.attendanceManagement.data.repository.incarico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.TeamRole;

/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface TeamRoleRepository extends JpaRepository<TeamRole, Integer>, 
							QuerydslPredicateExecutor<TeamRole>{

}
