package it.palex.attendanceManagement.batch.deleteChangePasswordHistory;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.batch.aspects.BatchAroundLogger;
import it.palex.attendanceManagement.batch.config.JobNamesAndGroup;
import it.palex.attendanceManagement.library.utils.DateUtility;


@Component
public class DeleteChangePasswordHistoryBatch {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteChangePasswordHistoryBatch.class);

	@Autowired
	private DeleteChangePasswordHistoryTransactionalService deleteChangePasswordHistoryTransactionalService;
	
	@Value("${security.change-password-history.days-to-store}")
	private int daysToStore;
	
	
	/*Cron expression means at 00:30
     */
	@Scheduled(cron = "${cron.delete_change_password_history_batch}")
	@BatchAroundLogger(batchName = JobNamesAndGroup.DELETE_CHANGE_PASSWORD_HISTORY_TASKLET.JOB_NAME)
	public void deleteChangePasswordHistory() throws Exception {
		
		try {
			Calendar date = DateUtility.getCurrentCalendarInUTC();
			date = DateUtility.addDaysToCalendar(date, this.daysToStore * - 1);
			
			int deleted = this.deleteChangePasswordHistoryTransactionalService.deleteAllSuccLoginLogsMadeBefore(date);
			
			LOGGER.info("Deleted "+deleted+" User Password change history");
		}catch(Exception e) {
			LOGGER.error("", e);
			throw e;
		}
		
	}

}
