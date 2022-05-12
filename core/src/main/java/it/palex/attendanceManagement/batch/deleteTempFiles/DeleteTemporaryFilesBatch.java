package it.palex.attendanceManagement.batch.deleteTempFiles;

import it.palex.attendanceManagement.batch.aspects.BatchAroundLogger;
import it.palex.attendanceManagement.batch.config.JobNamesAndGroup;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.data.service.user.SuccessfullyLoginLogsService;
import it.palex.attendanceManagement.library.service.ConfigurationsService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

/**
 * This batch deletes temporary files from
 */
@Component
public class DeleteTemporaryFilesBatch {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTemporaryFilesBatch.class);
	
	@Autowired
	private DocumentService documentService;

	@Autowired
	private ConfigurationsService configurationsService;

	/*
	 * Cron expression means --> every 30 minutes
     */
	@Scheduled(cron = "${cron.delete_temp_files_batch}")
	@BatchAroundLogger(batchName = JobNamesAndGroup.DELETE_TEMPORARY_FILES_TASKLET.JOB_NAME)
	public void deleteTempFiles() throws Exception {
		
		try {
			Calendar date = DateUtility.getCurrentCalendarInUTC();
			date = DateUtility.addSecondsToCalendar(date,
					this.configurationsService.getTempFileTimeToLiveSeconds() * - 1);
			
			int deleted = startTempFileCleaning(date);
			
			LOGGER.info("Deleted "+deleted+" temp files");
		}catch(Exception e) {
			LOGGER.error("", e);
			throw e;
		}
	}

	private int startTempFileCleaning(Calendar date) {
		int initialPageSize = 50;
		int errorsNum = 0;
		int elementsDeleted = 0;

		List<Document> tempDocs = this.documentService.findTempFilesCreatedBefore(
				date.getTime(), PageRequest.of(0, initialPageSize));

		// in this treset will be stored the files that generates error on delete
		TreeSet<Long> filesToSkip = new TreeSet<>();

		while(tempDocs.size()<= errorsNum){
			for (Document tempDoc: tempDocs) {

				//skip file if had caused yet a deletion error
				if(!filesToSkip.contains(tempDoc.getId())) {
					try {
						this.documentService.deleteDocumentAndFileWithTransact(tempDoc);
						elementsDeleted++;
					} catch (Exception e) {
						LOGGER.error("Error deleting document " + tempDoc.getId(), e);
						filesToSkip.add(tempDoc.getId());
						errorsNum++;
					}
				}
			}

			int totalElementsToRetrieve = initialPageSize + errorsNum;

			tempDocs = this.documentService.findTempFilesCreatedBefore(
					date.getTime(), PageRequest.of(0, totalElementsToRetrieve));
		}



		return elementsDeleted;
	}


}
