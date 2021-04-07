package it.palex.attendanceManagement.batch.deleteExpiredChangePasswordRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.batch.aspects.BatchAroundLogger;
import it.palex.attendanceManagement.batch.config.JobNamesAndGroup;
import it.palex.attendanceManagement.data.service.user.ResetPasswordRequestService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Component
public class DeleteExpiredChangePasswordRequestBatch {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteExpiredChangePasswordRequestBatch.class);

	@Autowired
	private ResetPasswordRequestService resetpasswordRequestSrv;
	
	
	/*
	 * Cron expression means --> every hour at min 35  
     */
	@Scheduled(cron = "${cron.delete_expired_change_password_request_batch}")
	@BatchAroundLogger(batchName = JobNamesAndGroup.DELETE_EXPIRED_CHANGE_PASSWORD_REQUEST.JOB_NAME)
	@Transactional(rollbackFor = Exception.class)
	public void deleteExpiredChangePasswordRequest() {
		try {
			int deletedRequests = this.resetpasswordRequestSrv
					.deleteAllResetPasswordExpiredBefore(DateUtility.getCurrentDateInUTC());
			
			LOGGER.info("Deleted ResetPasswordExpired "+deletedRequests);
		}catch(Exception e) {
			LOGGER.error("Error", e);
			throw e;
		}
	}
	
	

}