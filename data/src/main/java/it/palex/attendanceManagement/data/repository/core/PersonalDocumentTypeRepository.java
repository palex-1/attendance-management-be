package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.PersonalDocumentType;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 19 giu 2020
 */
@Repository
public interface PersonalDocumentTypeRepository extends JpaRepository<PersonalDocumentType, Integer>, 
								QuerydslPredicateExecutor<PersonalDocumentType>{

}
