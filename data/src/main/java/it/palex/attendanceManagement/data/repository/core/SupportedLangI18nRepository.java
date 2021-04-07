package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.SupportedLangI18n;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 2 giu 2020
 */
@Repository
public interface SupportedLangI18nRepository extends JpaRepository<SupportedLangI18n, Integer>,
				QuerydslPredicateExecutor<SupportedLangI18n> {

}
