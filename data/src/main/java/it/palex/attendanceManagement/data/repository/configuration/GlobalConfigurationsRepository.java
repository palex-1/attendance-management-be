package it.palex.attendanceManagement.data.repository.configuration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.GlobalConfigurations;


/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface GlobalConfigurationsRepository extends JpaRepository<GlobalConfigurations, Integer>, 
		QuerydslPredicateExecutor<GlobalConfigurations>{

}
