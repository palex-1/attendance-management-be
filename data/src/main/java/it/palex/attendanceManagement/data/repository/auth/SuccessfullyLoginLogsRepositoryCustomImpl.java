package it.palex.attendanceManagement.data.repository.auth;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.auth.SuccessfullyLoginLogs;
import it.palex.attendanceManagement.data.repository.generic.AbstractDAO;

/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public class SuccessfullyLoginLogsRepositoryCustomImpl extends AbstractDAO<SuccessfullyLoginLogs>
		implements SuccessfullyLoginLogsRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public SuccessfullyLoginLogsRepositoryCustomImpl() {
		super(SuccessfullyLoginLogs.class);
	}

	@Override
	public SuccessfullyLoginLogs getByKey(Integer idUserAuthDetails, String ip, String userAgent, Date loginDate) {
		if (idUserAuthDetails == null || ip == null || userAgent == null || loginDate == null) {
			throw new NullPointerException("idUserAuthDetails:" + idUserAuthDetails + "ip:" + ip + " userAgent:"
					+ userAgent + " loginDate:" + loginDate);
		}

		TypedQuery<SuccessfullyLoginLogs> query = (TypedQuery<SuccessfullyLoginLogs>) this.getEntityManager()
				.createNamedQuery("SuccessfullyLoginLogs.findByKey", SuccessfullyLoginLogs.class).setParameter("ip", ip)
				.setParameter("fkIdUsersAuthDetails", idUserAuthDetails).setParameter("userAgent", userAgent)
				.setParameter("loginDate", loginDate);

		List<SuccessfullyLoginLogs> res = this.find(query);

		if (res.isEmpty()) {
			return null;
		}

		return res.get(0);
	}

	@Override
	public List<SuccessfullyLoginLogs> getAllSuccessfullyLoginLogsOfUserAuthDetails(Integer idUserAuthDetails) {
		if (idUserAuthDetails == null) {
			throw new NullPointerException();
		}

		TypedQuery<SuccessfullyLoginLogs> query = (TypedQuery<SuccessfullyLoginLogs>) this.getEntityManager()
				.createNamedQuery("SuccessfullyLoginLogs.findByFkIdUsersAuthDetails", SuccessfullyLoginLogs.class)
				.setParameter("fkIdUsersAuthDetails", idUserAuthDetails);

		return this.find(query);
	}

	@Override
	public long countAllSuccessfullyLoginLogsOfUserAuthDetails(Integer idUserAuthDetails) {
		if (idUserAuthDetails == null) {
			throw new NullPointerException();
		}

		TypedQuery<Long> query = (TypedQuery<Long>) this.getEntityManager()
				.createNamedQuery("SuccessfullyLoginLogs.countByFkIdUsersAuthDetails", Long.class)
				.setParameter("fkIdUsersAuthDetails", idUserAuthDetails);

		return this.executeCountQuery(query);
	}

	@Override
	public List<SuccessfullyLoginLogs> getSuccessfullyLoginLogsOfUserAuthDetailsInTimeRange(Integer idUserAuthDetails,
			Calendar from, Calendar to) {
		if (idUserAuthDetails == null || from == null || to == null) {
			throw new NullPointerException();
		}
		if (from.compareTo(to) > 0) {
			throw new IllegalArgumentException("From after to!! from:" + from + " to:" + to);
		}

		TypedQuery<SuccessfullyLoginLogs> query = (TypedQuery<SuccessfullyLoginLogs>) this.getEntityManager()
				.createNamedQuery("SuccessfullyLoginLogs.findSuccessfullyLoginLogsOfUserAuthDetailsInTimeRange",
						SuccessfullyLoginLogs.class)
				.setParameter("fkIdUsersAuthDetails", idUserAuthDetails).setParameter("from", from)
				.setParameter("to", to);

		return this.find(query);

	}

	@Override
	public long countAllSuccessfullyLoginLogsOfUserAuthDetailsInTimeRange(Integer idUserAuthDetails, Calendar from,
			Calendar to) {
		if (idUserAuthDetails == null || from == null || to == null) {
			throw new NullPointerException();
		}
		if (from.compareTo(to) > 0) {
			throw new IllegalArgumentException("From after to!! from:" + from + " to:" + to);
		}

		TypedQuery<Long> query = (TypedQuery<Long>) this.getEntityManager()
				.createNamedQuery("SuccessfullyLoginLogs.countSuccessfullyLoginLogsOfUserAuthDetailsInTimeRange",
						Long.class)
				.setParameter("fkIdUsersAuthDetails", idUserAuthDetails).setParameter("from", from.getTime())
				.setParameter("to", to.getTime());

		return this.executeCountQuery(query);

	}

	@Override
	public int clearLoginHistoryOfUserAuthDetails(Integer idUserAuthDetails) {
		if (idUserAuthDetails == null) {
			throw new NullPointerException();
		}

		Query query = (Query) this.getEntityManager()
				.createNamedQuery("SuccessfullyLoginLogs.deleteLoginHistoryOfUserAuthDetails")
				.setParameter("fkIdUsersAuthDetails", idUserAuthDetails);

		return this.executeDeleteOrUpdateQuery(query);
	}

	@Override
	public int clearLoginHistoryOfUserAuthDetailsBeforeDate(Integer idUserAuthDetails, Calendar beforeDate) {
		if (idUserAuthDetails == null || beforeDate == null) {
			throw new NullPointerException("idUserAuthDetails:" + idUserAuthDetails + " beforeDate:" + beforeDate);
		}

		Query query = (Query) this.getEntityManager()
				.createNamedQuery("SuccessfullyLoginLogs.deleteLoginHistoryOfUserAuthDetailsBeforeDate")
				.setParameter("fkIdUsersAuthDetails", idUserAuthDetails)
				.setParameter("beforeDate", beforeDate.getTime());

		return this.executeDeleteOrUpdateQuery(query);

	}

	@Override
	public int clearLoginHistoryOfAllAccountBeforeDate(Calendar beforeDate) {
		if (beforeDate == null) {
			throw new NullPointerException();
		}

		Query query = (Query) this.getEntityManager()
				.createNamedQuery("SuccessfullyLoginLogs.deleteLoginHistoryOfAllAccountBeforeDate")
				.setParameter("beforeDate", beforeDate.getTime());

		return this.executeDeleteOrUpdateQuery(query);
	}

	@Override
	public long countAllSuccessfullyLoginLogs() {
		TypedQuery<Long> query = (TypedQuery<Long>) this.getEntityManager()
				.createNamedQuery("SuccessfullyLoginLogs.countAllSuccessfullyLoginLogs", Long.class);

		return this.executeCountQuery(query);

	}

}
