package it.palex.attendanceManagement.data.repository.documento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.Document;


@Repository
public interface DocumentRepository  extends JpaRepository<Document, Long>, 
			QuerydslPredicateExecutor<Document>{

}
