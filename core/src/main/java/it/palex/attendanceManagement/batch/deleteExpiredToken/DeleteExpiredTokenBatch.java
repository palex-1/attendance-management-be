package it.palex.attendanceManagement.batch.deleteExpiredToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.batch.aspects.BatchAroundLogger;
import it.palex.attendanceManagement.batch.config.JobNamesAndGroup;
import it.palex.attendanceManagement.data.service.auth.UserAccessTokenService;

@Component
public class DeleteExpiredTokenBatch {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteExpiredTokenBatch.class);

	@Autowired
	private UserAccessTokenService userAccessTokenSrv;
	

	/*Cron expression means at minut 17 of every hour so  will be executed at 19:17, 20:17 etc...
     */
	@Scheduled(cron = "${cron.delete_expired_token_batch}")
	@BatchAroundLogger(batchName = JobNamesAndGroup.DELETE_EXPIRED_TOKEN.JOB_NAME)
	@Transactional(rollbackFor = Exception.class)
	public void deleteExpiredToken() {
		try {
			int deletedTokens = this.userAccessTokenSrv.deleteAllExpiredTokens();
			LOGGER.info("Deleted tokens "+deletedTokens);
		}catch(Exception e) {
			LOGGER.error("Error", e);
			throw e;
		}
	}
	
	

}