package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.WorkTransferRequest;


/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 4 lug 2020
 */
@Repository
public interface WorkTransferRequestRepository extends JpaRepository<WorkTransferRequest, Long>, QuerydslPredicateExecutor<WorkTransferRequest> {

}
