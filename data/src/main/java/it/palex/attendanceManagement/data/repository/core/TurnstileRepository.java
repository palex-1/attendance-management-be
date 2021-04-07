package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.Turnstile;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 26 giu 2020
 */
@Repository
public interface TurnstileRepository extends JpaRepository<Turnstile, Long>,
										QuerydslPredicateExecutor<Turnstile> {

}
