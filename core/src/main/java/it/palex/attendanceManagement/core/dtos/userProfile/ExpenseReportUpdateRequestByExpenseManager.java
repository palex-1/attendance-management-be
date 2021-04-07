package it.palex.attendanceManagement.core.dtos.userProfile;

public class ExpenseReportUpdateRequestByExpenseManager {

	private Long expenseReportId;
	private String status;
	private String notes;

	public Long getExpenseReportId() {
		return expenseReportId;
	}

	public void setExpenseReportId(Long expenseReportId) {
		this.expenseReportId = expenseReportId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
