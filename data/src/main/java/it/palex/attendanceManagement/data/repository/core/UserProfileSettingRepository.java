package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.UserProfileSetting;

@Repository
public interface UserProfileSettingRepository  extends JpaRepository<UserProfileSetting, Long>,
	QuerydslPredicateExecutor<UserProfileSetting> {

}
