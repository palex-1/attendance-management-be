package it.palex.attendanceManagement.data.repository.auth;

import java.util.Date;
import java.util.List;

import it.palex.attendanceManagement.data.entities.auth.ResetPasswordRequest;

/**
 * @author Alessandro Pagliaro
 *
 */
public interface ResetPasswordRequestRepositoryCustom {

	/**
	 * @param ip
	 * @param userAgent
	 * @param creationDate
	 * @param username
	 * @return
	 */
	public ResetPasswordRequest getByKey(String ip, String userAgent, Date creationDate, String username);

	/**
	 * @param requestToken
	 * @return
	 */
	public ResetPasswordRequest getByToken(String requestToken);

	/**
	 * @param ip
	 * @param from
	 * @param to
	 * @return
	 */
	public long countResetPasswordRequestOfIpInTimeRange(String ip, Date from, Date to);

	/**
	 * @param username
	 * @param from
	 * @param to
	 * @return
	 */
	public long countResetPasswordRequestOfUsernameInTimeRange(String username, Date from, Date to);

	/**
	 * @param username
	 * @param currentDate
	 * @return
	 */
	public boolean existRecoveryAttemptForUserInDate(String username, Date currentDate);

	public int deleteAllPasswordResetRequestBeforeDateOfUsername(String username, Date time);

	/**
	 * @param username
	 * @return
	 */
	public List<ResetPasswordRequest> getAllResetPasswordRequestOfUsername(String username);
	
	public int deleteAllResetPasswordExpiredBefore(Date beforeDate);

}
