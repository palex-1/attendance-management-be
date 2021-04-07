package it.palex.attendanceManagement.core.dtos.tasks;

public class CompletionLockAddRequest {

	private Integer year;
	private Integer month;
	private Boolean hoursCalculationExecutionRequested;

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

}
