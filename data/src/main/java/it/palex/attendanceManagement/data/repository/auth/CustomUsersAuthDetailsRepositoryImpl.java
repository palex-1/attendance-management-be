package it.palex.attendanceManagement.data.repository.auth;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.repository.generic.AbstractDAO;

@Repository
public class CustomUsersAuthDetailsRepositoryImpl extends AbstractDAO<UsersAuthDetails> implements CustomUsersAuthDetailsRepository{
	
	@PersistenceContext
    private EntityManager em;
	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public CustomUsersAuthDetailsRepositoryImpl() {
		super(UsersAuthDetails.class);
	}

	@Override
	public UsersAuthDetails findByIdCustom(Integer id) {
		TypedQuery<UsersAuthDetails> query = this.getEntityManager()
				.createNamedQuery("UsersAuthDetails.findById", UsersAuthDetails.class)
				.setParameter("id", id);
		
		List<UsersAuthDetails> res = this.find(query);
		if(res.isEmpty()){
			return null;
		}
		if(res.size()>1){
			throw new RuntimeException("Two UsersAuthDetails has the same id");
		}
		return res.get(0);
	}

	
	@Override
	public UsersAuthDetails findByHashedUsername(String username) {
		TypedQuery<UsersAuthDetails> query = this.getEntityManager()
				.createNamedQuery("UsersAuthDetails.findByUsername", UsersAuthDetails.class)
				.setParameter("username", username);
		
		List<UsersAuthDetails> res = this.find(query);
		if(res.isEmpty()){
			return null;
		}
		if(res.size()>1){
			throw new RuntimeException("Two UsersAuthDetails has the same username");
		}
		return res.get(0);
	}
	
	
	
}
