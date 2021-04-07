package it.palex.attendanceManagement.core.dtos.userProfile;

import java.util.Date;

public class ExpenseReportUpdateRequest {
	
	private Long expenseReportId;
	private String title;
	private Date dateOfExpence;
	private String location;

	public Long getExpenseReportId() {
		return expenseReportId;
	}

	public void setExpenseReportId(Long expenseReportId) {
		this.expenseReportId = expenseReportId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDateOfExpence() {
		return dateOfExpence;
	}

	public void setDateOfExpence(Date dateOfExpence) {
		this.dateOfExpence = dateOfExpence;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
