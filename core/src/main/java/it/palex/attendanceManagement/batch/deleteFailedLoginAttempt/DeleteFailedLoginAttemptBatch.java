package it.palex.attendanceManagement.batch.deleteFailedLoginAttempt;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.batch.aspects.BatchAroundLogger;
import it.palex.attendanceManagement.batch.config.JobNamesAndGroup;
import it.palex.attendanceManagement.data.service.user.FailedLoginAttemptService;
import it.palex.attendanceManagement.library.utils.DateUtility;


@Component
public class DeleteFailedLoginAttemptBatch {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFailedLoginAttemptBatch.class);

	@Autowired
	private FailedLoginAttemptService failedLoginAttemptService;
	
	
	@Value("${security.failed-login-attempt-logs.days-to-store}")
	private int daysToStore;
	
	
	/*
	 * Cron expression means at minute 0 of every hour so  will be executed at 19:00, 20:00, 21:00 etc...
     */
	@Scheduled(cron = "${cron.delete_failed_login_attempt_batch}")
	@BatchAroundLogger(batchName = JobNamesAndGroup.DELETE_FAILED_LOGIN_ATTEMPT_TASKLET.JOB_NAME)
	@Transactional(rollbackFor = Exception.class)
	public void deleteFailedLoginAttempt() throws Exception {
		
		try {
			Calendar date = DateUtility.getCurrentCalendarInUTC();
			date = DateUtility.addDaysToCalendar(date, this.daysToStore * - 1);
			
			int deleted = this.failedLoginAttemptService.deleteAllFailedLoginAttemptMadeBefore(date);
			LOGGER.info("Deleted "+deleted+" Failed login attempt");
			
		}catch(Exception e) {
			LOGGER.error("", e);
			throw e;
		}
	}
	
	
	

}
