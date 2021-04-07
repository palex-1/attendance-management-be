package it.palex.attendanceManagement.batch.report;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.batch.aspects.BatchAroundLogger;
import it.palex.attendanceManagement.batch.config.JobNamesAndGroup;
import it.palex.attendanceManagement.batch.report.generator.ReportUserTaskGenerator;
import it.palex.attendanceManagement.data.entities.core.ReportUserTask;
import it.palex.attendanceManagement.data.service.core.ReportUserTaskService;

@Component
public class TaskReportBatch {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskReportBatch.class);
			
	@Autowired
	private ReportUserTaskService reportUserTaskService;
	
	@Autowired
	private ReportUserTaskGenerator generator;
	
	
	@Scheduled(cron = "${cron.task_report_batch}")
	@BatchAroundLogger(batchName = JobNamesAndGroup.TASK_REPORT_BATCH.JOB_NAME)
	public void startTaskReportGeneration() {
		
		Sort sort = Sort.by(Direction.ASC, "createdDate");

		int page = 0;
		List<ReportUserTask> reports = this.reportUserTaskService.findAllReportToBeDone(PageRequest.of(page, 1, sort));
		
		while(!reports.isEmpty()) {
		
			ReportUserTask toStart = reports.get(0);
			
			try {
				LOGGER.debug("Started generation of report "+toStart);
				
				this.generator.startReportGeneration(toStart);
				
				LOGGER.debug("Successfully completed generation of report "+toStart);
			}catch(Exception e) {
				page++;
				LOGGER.error("Error during generation of report "+toStart, e);
			}
			
			reports = this.reportUserTaskService.findAllReportToBeDone(PageRequest.of(page, 1, sort));
		}
		
		
		
	}
	
	

}
