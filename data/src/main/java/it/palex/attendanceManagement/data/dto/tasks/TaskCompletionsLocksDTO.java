package it.palex.attendanceManagement.data.dto.tasks;

import java.util.Date;

public class TaskCompletionsLocksDTO {

	private Integer id;
	private Integer year;
	private Integer month;
	private Boolean hoursCalculationExecutionRequested;
	private String status;
	private Date processedOnDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Boolean getHoursCalculationExecutionRequested() {
		return hoursCalculationExecutionRequested;
	}

	public void setHoursCalculationExecutionRequested(Boolean hoursCalculationExecutionRequested) {
		this.hoursCalculationExecutionRequested = hoursCalculationExecutionRequested;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getProcessedOnDate() {
		return processedOnDate;
	}

	public void setProcessedOnDate(Date processedOnDate) {
		this.processedOnDate = processedOnDate;
	}

}
