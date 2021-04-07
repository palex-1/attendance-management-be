package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.Company;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer>,
				QuerydslPredicateExecutor<Company> {

}
