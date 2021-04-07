package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.ExpenseReportElement;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 7 lug 2020
 */
@Repository
public interface ExpenseReportElementRepository extends JpaRepository<ExpenseReportElement, Long>, QuerydslPredicateExecutor<ExpenseReportElement> {

}
