package it.palex.attendanceManagement.data.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;

@Repository
public interface UsersAuthDetailsRepository  extends JpaRepository<UsersAuthDetails, Integer>, 
						QuerydslPredicateExecutor<UsersAuthDetails>, CustomUsersAuthDetailsRepository {

}


