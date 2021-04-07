package it.palex.attendanceManagement.data.repository.auth;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.UserContacts;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface UserContactsRepository extends JpaRepository<UserContacts, Integer>, 
    QuerydslPredicateExecutor<UserContacts> {

	@Modifying
	@Query("UPDATE UserContacts contact set contact.verificationToken = NULL "
			+ "where contact.verificationTokenExpirationDate < ?1")
	public int deleteExpiredTokensBefore(Date expiredBefore);

	@Modifying
	@Query("DELETE FROM UserContacts contact WHERE contact.id=?1")
	public void deleteByIdCustom(Integer id);

}
