package it.palex.attendanceManagement.data.repository.core;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.UserNotification;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long>,
	QuerydslPredicateExecutor<UserNotification> {

	@Modifying
	@Query("delete from UserNotification where createDate < ?1")
	public int deleteAllNotificationCreatedBefore(Date date);

}
