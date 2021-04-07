package it.palex.attendanceManagement.batch.report.generator;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import it.palex.attendanceManagement.data.entities.CompletedTask;
import it.palex.attendanceManagement.data.entities.FoodVoucherRequest;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.ExpenseReport;
import it.palex.attendanceManagement.data.entities.core.WorkTransferRequest;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 3 lug 2020
 */
public interface ReportGenerator {
	
	/**
	 * 
	 * @param profile
	 * @param completedTasksOfUserInMonth
	 * @param workTransferRequest 
	 * @param foodVoucherReq 
	 * @param year
	 * @param month
	 * @return the report for the specified user with the extension. Pair -file,extension-
	 * @throws Exception 
	 */
	public Pair<InputStream, String> generateReport(UserProfile profile, List<CompletedTask> completedTasksOfUserInMonth, 
			List<FoodVoucherRequest> foodVoucherReq, List<WorkTransferRequest> workTransferRequest, List<ExpenseReport> expenseReport,
			List<Calendar> datesOfYearToBeNotWorked , int year, int month, Integer userWorkingHours) throws Exception;
	
	
	
	
	
}
