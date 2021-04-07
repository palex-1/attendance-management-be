package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.Paycheck;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 16 giu 2020
 */
@Repository
public interface PaycheckRepository extends JpaRepository<Paycheck, Long>, QuerydslPredicateExecutor<Paycheck>{

}
