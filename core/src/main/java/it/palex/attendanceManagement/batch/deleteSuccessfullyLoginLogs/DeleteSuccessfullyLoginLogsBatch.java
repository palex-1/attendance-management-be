package it.palex.attendanceManagement.batch.deleteSuccessfullyLoginLogs;

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
import it.palex.attendanceManagement.data.service.user.SuccessfullyLoginLogsService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Component
public class DeleteSuccessfullyLoginLogsBatch {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteSuccessfullyLoginLogsBatch.class);
	
	@Autowired
	private SuccessfullyLoginLogsService succLoginLogsSrv;
	
	@Value("${security.successfully-login-logs.days-to-store}")
	private int daysToStore;
	
	/*
	 * Cron expression means --> every day at 00:10
     */
	@Scheduled(cron = "${cron.delete_successfully_login_logs_batch}")
	@BatchAroundLogger(batchName = JobNamesAndGroup.DELETE_SUCCESSFULLY_LOGIN_LOGS_TASKLET.JOB_NAME)
	@Transactional(rollbackFor = Exception.class)
	public void deleteSuccessfullyLoginLogs() throws Exception {
		
		try {
			Calendar date = DateUtility.getCurrentCalendarInUTC();
			date = DateUtility.addDaysToCalendar(date, this.daysToStore * - 1);
			
			int deleted = this.succLoginLogsSrv.deleteAllSuccLoginLogsMadeBefore(date);
			
			LOGGER.info("Deleted "+deleted+" Successfully login logs");
		}catch(Exception e) {
			LOGGER.error("", e);
			throw e;
		}
	}

}
