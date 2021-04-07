package it.palex.attendanceManagement.batch.report.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import it.palex.attendanceManagement.commons.utils.document.MultipleDocumentBuilderFactory;
import it.palex.attendanceManagement.commons.utils.tables.Table;
import it.palex.attendanceManagement.data.entities.CompletedTask;
import it.palex.attendanceManagement.data.entities.FoodVoucherRequest;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.ExpenseReport;
import it.palex.attendanceManagement.data.entities.core.WorkTransferRequest;

public class StandardReportGeneratorXLSX extends StandardExcelReportGeneratorAbstract implements ReportGenerator {
	
	@Override
	public Pair<InputStream, String> generateReport(UserProfile profile, List<CompletedTask> completedTasksOfUserInMonth, 
			List<FoodVoucherRequest> foodVoucherReq, List<WorkTransferRequest> workTransferRequest, List<ExpenseReport> expenseReport,
			List<Calendar> datesOfYearToBeNotWorked, int year, int month, Integer userWorkingHours) throws IOException {
				
		ArrayList<Table> sheets = this.buildSheets(profile, completedTasksOfUserInMonth, foodVoucherReq, workTransferRequest,
				expenseReport, datesOfYearToBeNotWorked, year, month, userWorkingHours);
		
		InputStream fileStream = MultipleDocumentBuilderFactory.build(MultipleDocumentBuilderFactory.XLSXBuilderID, sheets);
		
		return Pair.of(fileStream, MultipleDocumentBuilderFactory.XLSXBuilderID);
	}

	
	
}
