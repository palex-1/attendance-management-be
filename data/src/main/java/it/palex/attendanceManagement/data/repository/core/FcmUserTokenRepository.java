package it.palex.attendanceManagement.data.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.core.FcmUserToken;

@Repository
public interface FcmUserTokenRepository extends JpaRepository<FcmUserToken, Long>,
		QuerydslPredicateExecutor<FcmUserToken> {

}