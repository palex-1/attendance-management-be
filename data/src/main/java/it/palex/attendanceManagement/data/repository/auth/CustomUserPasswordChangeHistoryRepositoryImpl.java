/**
 * 
 */
package it.palex.attendanceManagement.data.repository.auth;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.UserPasswordChangeHistory;
import it.palex.attendanceManagement.data.repository.generic.AbstractDAO;

/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public class CustomUserPasswordChangeHistoryRepositoryImpl extends AbstractDAO<UserPasswordChangeHistory> implements CustomUserPasswordChangeHistoryRepository{

	@PersistenceContext
    private EntityManager em;
	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public CustomUserPasswordChangeHistoryRepositoryImpl() {
		super(UserPasswordChangeHistory.class);
	}

	@Override
	public int deleteAllChangePasswordOfUserBefore(Integer fkIdUsersAuthDetails, Date before) {
		if(fkIdUsersAuthDetails==null || before==null){
	    	  throw new NullPointerException("userId or before cannot be null at deleteAllChangePasswordOfUserBefore");
	    }
		Query deleteQuery = this.getEntityManager()
				.createNamedQuery("UserPasswordChangeHistory.deleteAllChangePasswordOfUserBeforeByUserId")
				.setParameter("fkIdUsersAuthDetails", fkIdUsersAuthDetails)
				.setParameter("beforeDate", before);
		
		return this.executeDeleteOrUpdateQuery(deleteQuery);
	}
		    
		   
	@Override
	public int deleteAllChangePasswordBefore(Date before) {
		if(before==null){
	    	  throw new NullPointerException("before cannot be null at deleteAllChangePasswordBefore");
	      }
		Query deleteQuery = this.getEntityManager()
				.createNamedQuery("UserPasswordChangeHistory.deleteAllChangePasswordBefore")
				.setParameter("beforeDate", before);
		
		return this.executeDeleteOrUpdateQuery(deleteQuery);
	}
	
	

}
