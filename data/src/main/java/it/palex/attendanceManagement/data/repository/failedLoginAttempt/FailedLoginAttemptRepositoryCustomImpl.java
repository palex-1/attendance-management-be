package it.palex.attendanceManagement.data.repository.failedLoginAttempt;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.FailedLoginAttempt;
import it.palex.attendanceManagement.data.repository.generic.AbstractDAO;

/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public class FailedLoginAttemptRepositoryCustomImpl extends AbstractDAO<FailedLoginAttempt>
		implements FailedLoginAttemptRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public FailedLoginAttemptRepositoryCustomImpl() {
		super(FailedLoginAttempt.class);
	}

	@Override
	public boolean existsAnAttemptMadeByIpForUsernameInDate(String ip, Date loginDate, String username) {
		if (ip == null || loginDate == null || username == null) {
			throw new NullPointerException(" ip:" + ip + " loginDate:" + loginDate + " username:" + username);
		}

		TypedQuery<FailedLoginAttempt> query = (TypedQuery<FailedLoginAttempt>) this.getEntityManager()
				.createNamedQuery("FailedLoginAttempt.findByIpUsernameAndDate", FailedLoginAttempt.class)
				.setParameter("loginDate", loginDate).setParameter("username", username).setParameter("ip", ip);

		List<FailedLoginAttempt> res = this.find(query);

		return !res.isEmpty();
	}

	@Override
	public FailedLoginAttempt getByKey(String userAgent, String ip, Date loginDate, String username) {
		if (userAgent == null || ip == null || loginDate == null || username == null) {
			throw new NullPointerException(
					"userAgent:" + userAgent + " ip:" + ip + " loginDate:" + loginDate + " username:" + username);
		}

		TypedQuery<FailedLoginAttempt> query = (TypedQuery<FailedLoginAttempt>) this.getEntityManager()
				.createNamedQuery("FailedLoginAttempt.findByKey", FailedLoginAttempt.class)
				.setParameter("loginDate", loginDate).setParameter("userAgent", userAgent)
				.setParameter("username", username).setParameter("ip", ip);

		List<FailedLoginAttempt> res = this.find(query);

		if (res.isEmpty()) {
			return null;
		}
		return res.get(0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistence.service.interfaces.FailedLoginAttemptServiceInterface#
	 * getAllFailedLoginAttemptOfIpInTimeRange(java.lang.String, java.util.Calendar,
	 * java.util.Calendar)
	 */
	@Override
	public List<FailedLoginAttempt> getAllFailedLoginAttemptOfIpInTimeRange(String ip, Calendar from, Calendar to) {
		if (ip == null || from == null || to == null) {
			throw new NullPointerException("ip:" + ip + " from:" + from + " to:" + to);
		}
		if (from.compareTo(to) > 0) {
			throw new IllegalArgumentException("From after to!! from:" + from + " to:" + to);
		}
		TypedQuery<FailedLoginAttempt> query = (TypedQuery<FailedLoginAttempt>) this.getEntityManager()
				.createNamedQuery("FailedLoginAttempt.findAllLoginAttemptOfIpInTimeRange", FailedLoginAttempt.class)
				.setParameter("from", from.getTime()).setParameter("to", to.getTime()).setParameter("ip", ip);

		return this.find(query);
	}

	@Override
	public long getCountOfAllFailedLoginAttemptOfIpInTimeRange(String ip, Calendar from, Calendar to) {
		if (ip == null || from == null || to == null) {
			throw new NullPointerException("ip:" + ip + " from:" + from + " to:" + to);
		}

		TypedQuery<Long> query = (TypedQuery<Long>) this.getEntityManager()
				.createNamedQuery("FailedLoginAttempt.countAllLoginAttemptOfIpInTimeRange", Long.class)
				.setParameter("from", from.getTime()).setParameter("to", to.getTime()).setParameter("ip", ip);

		return this.executeCountQuery(query);

	}

	@Override
	public List<FailedLoginAttempt> getAllFailedLoginAttemptOfIpAndUsernameInTimeRange(String ip, String username,
			Calendar from, Calendar to) {
		if (ip == null || username == null || from == null || to == null) {
			throw new NullPointerException("ip:" + ip + " username:" + username + " from:" + from + " to:" + to);
		}

		TypedQuery<FailedLoginAttempt> query = (TypedQuery<FailedLoginAttempt>) this.getEntityManager()
				.createNamedQuery("FailedLoginAttempt.findAllLoginAttemptOfIpAndUsernameInTimeRange",
						FailedLoginAttempt.class)
				.setParameter("from", from.getTime()).setParameter("to", to.getTime()).setParameter("ip", ip)
				.setParameter("username", username);

		return this.find(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistence.service.interfaces.FailedLoginAttemptServiceInterface#
	 * getCountOfAllFailedLoginAttemptOfUsernameInRange(java.lang.String,
	 * java.lang.String, java.util.Calendar, java.util.Calendar)
	 */
	@Override
	public long getCountOfAllFailedLoginAttemptOfIpAndUsernameInRange(String ip, String username, Calendar from,
			Calendar to) {
		if (ip == null || username == null || from == null || to == null) {
			throw new NullPointerException("ip:" + ip + " username:" + username + " from:" + from + " to:" + to);
		}

		TypedQuery<Long> query = (TypedQuery<Long>) this.getEntityManager()
				.createNamedQuery("FailedLoginAttempt.countAllLoginAttemptOfIpAndUsernameInTimeRange", Long.class)
				.setParameter("from", from.getTime()).setParameter("to", to.getTime()).setParameter("ip", ip)
				.setParameter("username", username);

		return this.executeCountQuery(query);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistence.service.interfaces.FailedLoginAttemptServiceInterface#
	 * deleteAllFailedLoginAttemptBefore(java.util.Calendar)
	 */
	@Override
	public int deleteAllFailedLoginAttemptBefore(Calendar beforeDate) {
		if (beforeDate == null) {
			throw new NullPointerException();
		}

		Query query = (Query) this.getEntityManager()
				.createNamedQuery("FailedLoginAttempt.deleteAllFailedLoginAttemptBefore")
				.setParameter("beforeDate", beforeDate.getTime());

		return this.executeDeleteOrUpdateQuery(query);
	}

}
