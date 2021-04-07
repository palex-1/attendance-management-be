package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.UserAttendance;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 26 giu 2020
 */
@Repository
public interface UserAttendanceRepository extends JpaRepository<UserAttendance, Long>,
										QuerydslPredicateExecutor<UserAttendance> {

}
