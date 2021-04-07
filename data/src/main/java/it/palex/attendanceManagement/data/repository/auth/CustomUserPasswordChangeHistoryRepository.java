/**
 * 
 */
package it.palex.attendanceManagement.data.repository.auth;

import java.util.Date;

/**
 * @author Alessandro Pagliaro
 *
 */
public interface CustomUserPasswordChangeHistoryRepository {

	/**
	 * 
	 * @param userId user id
	 * @param before the date before which entities are deleted
	 * @return the number of deleted entities
	 * @throws NullPointerException if <strong>id</strong> or <strong>before</strong> are null
	 */
	public int deleteAllChangePasswordOfUserBefore(Integer userId, Date before);
	
	/**
	 * 
	 * @param before the date before which entities are deleted
	 * @return the number of deleted entities
	 * @throws NullPointerException if <strong>before</strong> is null
	 */
	public int deleteAllChangePasswordBefore(Date before);
	
}
