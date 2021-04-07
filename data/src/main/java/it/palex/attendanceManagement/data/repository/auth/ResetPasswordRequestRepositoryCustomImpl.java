package it.palex.attendanceManagement.data.repository.auth;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.ResetPasswordRequest;
import it.palex.attendanceManagement.data.repository.generic.AbstractDAO;


/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public class ResetPasswordRequestRepositoryCustomImpl extends AbstractDAO<ResetPasswordRequest> 
						implements ResetPasswordRequestRepositoryCustom{

	@PersistenceContext
    private EntityManager em;
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public ResetPasswordRequestRepositoryCustomImpl() {
		super(ResetPasswordRequest.class);
	}
	
	@Override
	public ResetPasswordRequest getByKey(String ip, String userAgent, Date creationDate, String username){
		TypedQuery<ResetPasswordRequest> query=(TypedQuery<ResetPasswordRequest>) this.getEntityManager()
				.createNamedQuery("ResetPasswordRequest.findByKey",ResetPasswordRequest.class)
				.setParameter("userAgent", userAgent)
				.setParameter("creationDate", creationDate)
				.setParameter("ip", ip)
				.setParameter("username", username);
		
		List<ResetPasswordRequest> park = this.find(query);
		
		if(park.isEmpty()){
			return null;
		}
		return park.get(0);
	}
	
	
	@Override
	public ResetPasswordRequest getByToken(String requestToken){
		TypedQuery<ResetPasswordRequest> query=(TypedQuery<ResetPasswordRequest>) this.getEntityManager()
				.createNamedQuery("ResetPasswordRequest.findByRequestToken",ResetPasswordRequest.class)
				.setParameter("requestToken", requestToken);

		List<ResetPasswordRequest> park = this.find(query);
		
		if(park.isEmpty()){
			return null;
		}
		return park.get(0);
		
	}
	
	@Override
	public int deleteAllPasswordResetRequestBeforeDateOfUsername(String username, Date beforeDate) {
		Query query=(Query) this.getEntityManager()
				.createNamedQuery("ResetPasswordRequest.deleteAllPasswordResetRequestBeforeDateOfUsername")
				.setParameter("beforeDate", beforeDate)
				.setParameter("username", username);
		
		return this.executeDeleteOrUpdateQuery(query);
	}
	
	@Override
	public int deleteAllResetPasswordExpiredBefore(Date beforeDate) {
		Query query=(Query) this.getEntityManager()
				.createNamedQuery("ResetPasswordRequest.deleteAllResetPasswordExpiredBefore")
				.setParameter("beforeDate", beforeDate);
		
		return this.executeDeleteOrUpdateQuery(query);
	}
	
	
	@Override
	public long countResetPasswordRequestOfIpInTimeRange(String ip, Date from, Date to) {
		TypedQuery<Long> query=(TypedQuery<Long>) this.getEntityManager()
				.createNamedQuery("ResetPasswordRequest.countResetPasswordRequestOfIpInTimeRange", Long.class)
				.setParameter("ip", ip)
				.setParameter("from", from)
				.setParameter("to", to);
		
		return this.executeCountQuery(query);
	}
	
	@Override
	public long countResetPasswordRequestOfUsernameInTimeRange(String username, Date from,
			Date to) {
		TypedQuery<Long> query=(TypedQuery<Long>) this.getEntityManager()
					.createNamedQuery("ResetPasswordRequest.countResetPasswordRequestOfUsernameInTimeRange", Long.class)
					.setParameter("username", username)
					.setParameter("from", from)
					.setParameter("to", to);
			
		return this.executeCountQuery(query);
	}
	
	@Override
	public boolean existRecoveryAttemptForUserInDate(String username, Date currentDate) {
		TypedQuery<ResetPasswordRequest> query=(TypedQuery<ResetPasswordRequest>) this.getEntityManager()
					.createNamedQuery("ResetPasswordRequest.findByUserUsernameAndDate", ResetPasswordRequest.class)
					.setParameter("username", username)
					.setParameter("creationDate", currentDate);
		
		List<ResetPasswordRequest> list = this.find(query);
		
	    if(list.isEmpty()){
	    	return false;
	    }
		
	    return true;
	}
	
//	@Override
//	public int deleteAllResetPasswordRequestBefore(Date before) {
//		Query query=(Query) this.getEntityManager()
//				.createNamedQuery("ResetPasswordRequest.deleteAllResetPasswordRequestBefore")
//				.setParameter("beforeDate", before);
//		
//		return this.executeDeleteOrUpdateQuery(query);
//	}
	
	@Override
	public List<ResetPasswordRequest> getAllResetPasswordRequestOfUsername(String username) {
		TypedQuery<ResetPasswordRequest> query=(TypedQuery<ResetPasswordRequest>) this.getEntityManager()
				.createNamedQuery("ResetPasswordRequest.findByFkIdUsersAuthDetails", ResetPasswordRequest.class)
				.setParameter("username", username);

		List<ResetPasswordRequest> park = this.find(query);
		
		return park;
		
	}
}
