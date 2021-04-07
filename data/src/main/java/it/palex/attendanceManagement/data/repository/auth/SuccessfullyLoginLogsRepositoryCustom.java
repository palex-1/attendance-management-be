/**
 * 
 */
package it.palex.attendanceManagement.data.repository.auth;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.palex.attendanceManagement.data.entities.auth.SuccessfullyLoginLogs;

/**
 * @author Alessandro Pagliaro
 *
 */
public interface SuccessfullyLoginLogsRepositoryCustom {

	public List<SuccessfullyLoginLogs> getAllSuccessfullyLoginLogsOfUserAuthDetails(Integer idUserAuthDetails);
	
	public SuccessfullyLoginLogs getByKey(Integer idUserAuthDetails, String ip, String userAgent, Date loginDate);

	public long countAllSuccessfullyLoginLogsOfUserAuthDetails(Integer idUserAuthDetails);
	
	public List<SuccessfullyLoginLogs> getSuccessfullyLoginLogsOfUserAuthDetailsInTimeRange(Integer idUserAuthDetails, Calendar from, Calendar to);
	
	public long countAllSuccessfullyLoginLogsOfUserAuthDetailsInTimeRange(Integer idUserAuthDetails, Calendar from, Calendar to);
	
	public int clearLoginHistoryOfUserAuthDetails(Integer idUserAuthDetails);
	
	public int clearLoginHistoryOfUserAuthDetailsBeforeDate(Integer idUserAuthDetails, Calendar beforeDate);
	
	public int clearLoginHistoryOfAllAccountBeforeDate(Calendar beforeDate);
	
	public long countAllSuccessfullyLoginLogs();
	
}

