package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.UserProfileContractInfo;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 15 giu 2020
 */
@Repository
public interface UserProfileContractInfoRepository extends JpaRepository<UserProfileContractInfo, Integer>,
							QuerydslPredicateExecutor<UserProfileContractInfo> {

}
