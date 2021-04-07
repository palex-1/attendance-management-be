package it.palex.attendanceManagement.data.repository.auth;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.entities.auth.UsersAccessToken;
import it.palex.attendanceManagement.data.repository.generic.AbstractDAO;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@Component
public class UserAccessTokenRepositoryCustomImpl extends AbstractDAO<UsersAccessToken> 
		implements UserAccessTokenRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public EntityManager getEntityManager() {
	return em;
	}
	
	public UserAccessTokenRepositoryCustomImpl() {
	     super(UsersAccessToken.class);
	}
	
	@Override
	public UsersAccessToken findByTokenCustom(String token) {
		if(token==null) {
			return null;
		}
		
		TypedQuery<UsersAccessToken> query=(TypedQuery<UsersAccessToken>) this.getEntityManager()
				.createNamedQuery("UsersAccessToken.findByToken", UsersAccessToken.class)
				.setParameter("token", token);
		
		List<UsersAccessToken> res = this.find(query);
		
		if(res.isEmpty()){
			return null;
		}
		
		return res.get(0);
	}

	@Override
	public int deleteAllTokenOfUserId(Integer id) {
		if(id==null) {
			return 0;
		}
		Query query=(Query) this.getEntityManager()
				.createNamedQuery("UsersAccessToken.deleteAllTokenOfUserId")
				.setParameter("id", id);
		
		return this.executeDeleteOrUpdateQuery(query);
	}
	
	@Override
	public int deleteAllTokenExpiredBefore(Date date) {
		if(date==null) {
			return 0;
		}
		
		Query query=(Query) this.getEntityManager()
				.createNamedQuery("UsersAccessToken.deleteAllTokenExpiredBefore")
				.setParameter("date", date);
		
		return this.executeDeleteOrUpdateQuery(query);
	}
	
	
}
