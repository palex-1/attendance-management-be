package it.palex.attendanceManagement.data.repository.failedLoginAttempt;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.palex.attendanceManagement.data.entities.auth.FailedLoginAttempt;

/**
 * @author Alessandro Pagliaro
 *
 */
public interface FailedLoginAttemptRepositoryCustom {

	public boolean existsAnAttemptMadeByIpForUsernameInDate(String ip, Date loginDate, String username);

	public FailedLoginAttempt getByKey(String userAgent, String ip, Date loginDate, String username);

	public List<FailedLoginAttempt> getAllFailedLoginAttemptOfIpInTimeRange(String ip, Calendar from, Calendar to);

	public long getCountOfAllFailedLoginAttemptOfIpInTimeRange(String ip, Calendar from, Calendar to);

	public List<FailedLoginAttempt> getAllFailedLoginAttemptOfIpAndUsernameInTimeRange(String ip, String username,
			Calendar from, Calendar to);

	public long getCountOfAllFailedLoginAttemptOfIpAndUsernameInRange(String ip, String username, Calendar from,
			Calendar to);

	public int deleteAllFailedLoginAttemptBefore(Calendar beforeDate);
	
}