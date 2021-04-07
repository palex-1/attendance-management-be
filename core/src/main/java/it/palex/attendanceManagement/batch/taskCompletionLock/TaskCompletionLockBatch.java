package it.palex.attendanceManagement.batch.taskCompletionLock;

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
import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.core.TaskCompletionsLocks;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.entities.enumTypes.TaskCompletionLocksStatusEnum;
import it.palex.attendanceManagement.data.exceptions.InvalidConfigurationException;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.core.TaskCompletionsLocksService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.data.service.incarico.WorkTaskService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Component
public class TaskCompletionLockBatch {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskCompletionLockBatch.class);
			
	@Autowired
	private TaskCompletionsLocksService taskCompletionsLocksService;
	
	@Autowired
	private TaskCompletionLockEvaluatorService evaluator;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private GlobalConfigurationsService globalConfigurationsService;
	
	@Autowired
	private WorkTaskService workTaskService;
	
	
	@Scheduled(cron = "${cron.task_completion_lock_batch}")
	@BatchAroundLogger(batchName = JobNamesAndGroup.TASK_COMPLETIONS_LOCKS_BATCH.JOB_NAME)
	public void startCalculationGeneration() {
		
		Sort sort = Sort.by(Direction.ASC, "createdDate");

		int page = 0;
		List<TaskCompletionsLocks> locks = this.taskCompletionsLocksService.findAllLockToBeProcessed(PageRequest.of(page, 1, sort));
		
		while(!locks.isEmpty()) {
		
			TaskCompletionsLocks toEvaluate = locks.get(0);
			
			try {
				LOGGER.debug("Started completion lock evaluation "+toEvaluate);
				
				this.startTaskCompletionsLocksEvaluations(toEvaluate);
				
				LOGGER.debug("Successfully completion lock evaluation "+toEvaluate);
			}catch(Exception e) {
				page++;
				LOGGER.error("Error during lock evaluation "+toEvaluate, e);
			}
			
			locks = this.taskCompletionsLocksService.findAllLockToBeProcessed(PageRequest.of(page, 1, sort));
		}
		
	}

	
	private void startTaskCompletionsLocksEvaluations(TaskCompletionsLocks toEvaluate) {
		int page = 0;
		
		toEvaluate.setProcessedOnDate(DateUtility.getCurrentDateInUTC());
		toEvaluate.setStatus(TaskCompletionLocksStatusEnum.PROCESSING.name());
		this.taskCompletionsLocksService.saveOrUpdateAndFlush(toEvaluate);
		
		EvaluationInformation evaluationInformation = this.retriveEvaluationInformation();
		
		List<UserProfile> users = this.userProfileService.findAllUserWithActiveAccount(
				PageRequest.of(page, 10));
		
		
		while(!users.isEmpty()) {
			
			for(UserProfile user: users) {
				this.evaluateLockForUser(toEvaluate, user, evaluationInformation);
			}
			
			page++;
			
			users = this.userProfileService.findAllUserWithActiveAccount(PageRequest.of(page, 10));
		}
		
		
		toEvaluate.setStatus(TaskCompletionLocksStatusEnum.PROCESSED.name());
		this.taskCompletionsLocksService.saveOrUpdateAndFlush(toEvaluate);
	}
	
	
	private void evaluateLockForUser(TaskCompletionsLocks toEvaluate, UserProfile user,
			EvaluationInformation evaluationInformation) {
		try {
			LOGGER.debug("Starting lock evaluation "+toEvaluate+" for user "+user);
			
			this.evaluator.evaluateLockForUser(toEvaluate, user, evaluationInformation);
			
			LOGGER.debug("Completed lock evaluation "+toEvaluate+" for user "+user);
		}catch(Exception e) {
			LOGGER.error("Error during lock evaluation "+toEvaluate+" for user "+user, e);
		}
		
	}


	private EvaluationInformation retriveEvaluationInformation() {
		GlobalConfigurations vacationDayTaskCodeConfig = this.globalConfigurationsService.findByAreaAndKey(
				GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.AREA_NAME,  
				GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.FERIE);
    	
    	if(vacationDayTaskCodeConfig==null || vacationDayTaskCodeConfig.getSettingValue()==null) {
			throw new InvalidConfigurationException("Invalid area:"+GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.AREA_NAME+" "
					+ "key:"+GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.FERIE);
    	}
    	String vacationTaskCode = vacationDayTaskCodeConfig.getSettingValue();
    	
    	WorkTask vacationTask = workTaskService.findByTaskCode(vacationTaskCode);
    	if(vacationTask==null) {
			throw new InvalidConfigurationException("WORK TASK NOT FOUND. "+"Invalid area:"+GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.AREA_NAME+" "
					+ "key:"+GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.FERIE);
		}
    	
    	
    	
		GlobalConfigurations rolTaskCodeConfig = this.globalConfigurationsService.findByAreaAndKey(
				GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.AREA_NAME,  
				GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.ROL);
		
		if(rolTaskCodeConfig==null || rolTaskCodeConfig.getSettingValue()==null) {
			throw new InvalidConfigurationException("Invalid area:"+GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.AREA_NAME+" "
					+ "key:"+GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.ROL);
    	}
		
		String hoursOfLeaveTaskCode = rolTaskCodeConfig.getSettingValue();
		WorkTask hoursOfLeaveTask = workTaskService.findByTaskCode(hoursOfLeaveTaskCode);
		if(hoursOfLeaveTask==null) {
			throw new InvalidConfigurationException("WORK TASK NOT FOUND. "+"Invalid area:"+GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.AREA_NAME+" "
					+ "key:"+GlobalConfigurationSettingsTuple.CODICI_INCARICO_PER_CALCOLO_MENSILE.ROL);
		}
		
		EvaluationInformation informations = new EvaluationInformation(vacationTask, hoursOfLeaveTask);
		
		return informations;
	}
	
	
}
