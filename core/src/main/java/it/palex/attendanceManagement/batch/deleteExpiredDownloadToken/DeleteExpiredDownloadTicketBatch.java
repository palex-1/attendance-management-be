package it.palex.attendanceManagement.batch.deleteExpiredDownloadToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.batch.aspects.BatchAroundLogger;
import it.palex.attendanceManagement.batch.config.JobNamesAndGroup;
import it.palex.attendanceManagement.data.service.documento.TicketDownloadService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Component
public class DeleteExpiredDownloadTicketBatch {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteExpiredDownloadTicketBatch.class);

	@Autowired
	private TicketDownloadService ticketDownloadService;
	

	/*Cron expression means at minut 8 of every hour so  will be executed at 19:00, 20:00, 21:00 etc...
     */
	@Scheduled(cron = "${cron.delete_expired_download_ticket_batch}")
	@BatchAroundLogger(batchName = JobNamesAndGroup.DELETE_EXPIRED_DOWNLOAD_TICKET.JOB_NAME)
	@Transactional(rollbackFor = Exception.class)
	public void deleteExpiredDownloadTicket() {
		try {
			int deletedTokens = this.ticketDownloadService.deleteAllExpiredTicketsBefore(
					DateUtility.getCurrentDateInUTC());
			
			LOGGER.info(JobNamesAndGroup.DELETE_EXPIRED_DOWNLOAD_TICKET.JOB_NAME+": "
					+ "Deleted tokens "+deletedTokens);
			
		}catch(Exception e) {
			LOGGER.error("Error", e);
			throw e;
		}
	}
	


}
