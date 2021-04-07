package it.palex.attendanceManagement.batch.taskCompletionLock;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.UserProfileContractInfo;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.core.TaskCompletionsLocks;
import it.palex.attendanceManagement.data.entities.core.VacationAddRemoveLogs;
import it.palex.attendanceManagement.data.service.core.VacationAddRemoveLogsService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileContractInfoService;
import it.palex.attendanceManagement.data.service.incarico.CompletedTaskService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Component
public class TaskCompletionLockEvaluatorService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TaskCompletionLockEvaluatorService.class);
	
	@Autowired
	private CompletedTaskService completedTaskService;
	
	@Autowired
	private VacationAddRemoveLogsService vacationAddRemoveLogsService;
	
	@Autowired
	private UserProfileContractInfoService userProfileContractInfoService;
	
	
	
	
	@Transactional(rollbackFor = Exception.class)
	public void evaluateLockForUser(TaskCompletionsLocks toEvaluate, UserProfile user, EvaluationInformation evaluationInformation) {
		this.executeEvaluation(toEvaluate, user, evaluationInformation);
	}


	private void executeEvaluation(TaskCompletionsLocks toEvaluate, UserProfile user, EvaluationInformation evaluationInformation) {
		if(toEvaluate==null || user==null || evaluationInformation==null || evaluationInformation.getHoursOfLeaveTask()==null 
				|| evaluationInformation.getVacationTask()==null) {
			throw new NullPointerException("toEvaluate:"+toEvaluate+", user:"+user);
		}
		
		if(user.getUserProfileContractInfo()==null) {
			LOGGER.warn("User has no contract associated and the evaluation cannot be done. toEvaluate:"+toEvaluate+", user:"+user);
			return;
		}
		
		double percentage = calculatePercentageOfDaysWhereUserIsEmployeed(user, toEvaluate);
		
		int monthToEvaluate = toEvaluate.getMonth();
		int yearToEvaluate = toEvaluate.getYear();
		
		long hoursOfLeaveUsedInMonth = this.sumHoursRegisteredOnTask(evaluationInformation.getHoursOfLeaveTask(), yearToEvaluate, monthToEvaluate, user);
		long hoursOfVacationUsedInMonth = this.sumHoursRegisteredOnTask(
				evaluationInformation.getHoursOfLeaveTask(), yearToEvaluate, monthToEvaluate, user);
		
		
		double daysOfVacationUsed = ((double)hoursOfVacationUsedInMonth/(double)user.getUserProfileContractInfo().getWorkDayHours());
		
		double montlyLeaveHours = user.getUserProfileContractInfo().getLevel().getMonthlyLeaveHours();
		double montlyVacationDays = user.getUserProfileContractInfo().getLevel().getMonthlyVacationDays();
		
		double hoursOfLeaveToAdd = montlyLeaveHours * percentage;
		double daysOfVacationToAdd = montlyVacationDays * percentage;
		double hoursOfVacationToAdd = daysOfVacationToAdd * (double)user.getUserProfileContractInfo().getWorkDayHours();
		
		
		//save the added vacation hours
		this.saveVacationAddRemoveLogs(evaluationInformation.getVacationTask(), toEvaluate, hoursOfVacationToAdd , user);
				
		//save the removed vacation hours
		this.saveVacationAddRemoveLogs(evaluationInformation.getVacationTask(), toEvaluate, hoursOfVacationUsedInMonth * -1 , user);
				
				
		//save the added hours of leave
		this.saveVacationAddRemoveLogs(evaluationInformation.getHoursOfLeaveTask(), toEvaluate, hoursOfLeaveToAdd , user);
				
		//save the removed hours of leave
		this.saveVacationAddRemoveLogs(evaluationInformation.getHoursOfLeaveTask(), toEvaluate, hoursOfLeaveUsedInMonth * - 1, user);
		
		
		double diffHoursOfLeaveToAdd = Math.round((hoursOfLeaveToAdd - hoursOfLeaveUsedInMonth) * 100.0) / 100.0;
		double diffOfVacationDaysToAdd = Math.round((daysOfVacationToAdd - daysOfVacationUsed) * 100.0) / 100.0;
		
		
		UserProfileContractInfo contractInfo = user.getUserProfileContractInfo();
		contractInfo.setResidualLeaveHours(contractInfo.getResidualLeaveHours() + diffHoursOfLeaveToAdd);
		contractInfo.setResidualVacationDays(contractInfo.getResidualVacationDays() + diffOfVacationDaysToAdd);
		
		this.userProfileContractInfoService.saveOrUpdate(contractInfo);
	}

	
	
	private void saveVacationAddRemoveLogs(WorkTask workTask, TaskCompletionsLocks lock, double hoursAmount, UserProfile user) {
		VacationAddRemoveLogs log = new VacationAddRemoveLogs();
		log.setType(workTask.getTaskCode());
		log.setUserProfile(user);
		log.setAmountOfHours(hoursAmount);
		log.setLockId(lock.getId());
		
		this.vacationAddRemoveLogsService.saveOrUpdate(log);
	}
	
	
	private long sumHoursRegisteredOnTask(WorkTask task, int year, int month, UserProfile employee) {
		Calendar startRange = Calendar.getInstance();
		startRange.set(Calendar.YEAR, year);
		startRange.set(Calendar.MONTH, month);
		
		startRange.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endRange = Calendar.getInstance();
		endRange.set(Calendar.YEAR, year);
		endRange.set(Calendar.MONTH, month);
		int lastDayOfMonth = startRange.getActualMaximum(Calendar.DAY_OF_MONTH);
		endRange.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
		
		Date start = DateUtility.startOfDayOfDate(startRange.getTime());
		
		long hours = this.completedTaskService.sumRegisteredHoursMadeOnDateRangeByUser(task, employee,  
				start, DateUtility.endOfDayOfDate(endRange.getTime()));
		
		
		return hours;
	}
	
	

	private double calculatePercentageOfDaysWhereUserIsEmployeed(UserProfile user, TaskCompletionsLocks toEvaluate) {
		Calendar employmentDate = DateUtility.dateToCalendar(user.getDateOfEmployment());
		int yearOfEmployment = employmentDate.get(Calendar.YEAR);
		int monthOfEmployment = employmentDate.get(Calendar.MONTH);
		int dayOfEmployment = employmentDate.get(Calendar.DAY_OF_MONTH);
		
		final int ALL_WORKED_MONTH_PERCENTAGE = 1;
		final int NOT_WORKED_MONTH_PERCENTAGE = 0;
		
		int monthToEvaluate = toEvaluate.getMonth();
		int yearToEvaluate = toEvaluate.getYear();
		
		if(yearToEvaluate>yearOfEmployment) {
			return ALL_WORKED_MONTH_PERCENTAGE; //The user was employeed
		}
		
		if(yearToEvaluate<yearOfEmployment) {
			return NOT_WORKED_MONTH_PERCENTAGE; //The user was not employeed
		}
		
		//the year is the same
		
		if(monthToEvaluate>monthOfEmployment) {
			return ALL_WORKED_MONTH_PERCENTAGE; //The user was employeed
		}
		
		if(monthToEvaluate<monthOfEmployment) {
			return NOT_WORKED_MONTH_PERCENTAGE; //The user was not employeed
		}
		
		
		//month and year of employment is the same so the percentage will be  number of days of month/day of employment
		//days of month are the number of last day in month
		int daysOfMonth = employmentDate.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int workedDays = daysOfMonth - dayOfEmployment + 1;
		
		double percentageOfWorkedDays = ((double)workedDays)/((double)daysOfMonth);
		
		double roundedPercentage = Math.round(percentageOfWorkedDays * 100.0) / 100.0;
		
		return roundedPercentage;
	}
	
	
	

}
