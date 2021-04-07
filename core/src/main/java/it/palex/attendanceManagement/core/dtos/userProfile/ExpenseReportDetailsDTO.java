package it.palex.attendanceManagement.core.dtos.userProfile;

import java.util.List;

import it.palex.attendanceManagement.data.dto.core.ExpenseReportDTO;
import it.palex.attendanceManagement.data.dto.core.ExpenseReportElementDTO;

public class ExpenseReportDetailsDTO {

	private ExpenseReportDTO report;
	private List<ExpenseReportElementDTO> expenses;

	public ExpenseReportDTO getReport() {
		return report;
	}

	public void setReport(ExpenseReportDTO report) {
		this.report = report;
	}

	public List<ExpenseReportElementDTO> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<ExpenseReportElementDTO> expenses) {
		this.expenses = expenses;
	}

}
