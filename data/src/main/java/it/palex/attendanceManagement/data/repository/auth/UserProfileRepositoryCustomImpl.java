package it.palex.attendanceManagement.data.repository.auth;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.repository.generic.AbstractDAO;

@Repository
public class UserProfileRepositoryCustomImpl extends AbstractDAO<UserProfile> 
							implements UserProfileRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public UserProfileRepositoryCustomImpl() {
		super(UserProfile.class);
	}

	@Override
	public UserProfile persistForFirstTime(UserProfile profile) {
		this.em.persist(profile);
		
		return profile;
	}
	
	
}
