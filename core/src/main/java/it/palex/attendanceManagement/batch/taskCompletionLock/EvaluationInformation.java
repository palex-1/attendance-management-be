package it.palex.attendanceManagement.batch.taskCompletionLock;

import it.palex.attendanceManagement.data.entities.WorkTask;

public class EvaluationInformation {

	private WorkTask vacationTask;
	private WorkTask hoursOfLeaveTask;

	
	public EvaluationInformation(WorkTask vacationTask, WorkTask hoursOfLeaveTask) {
		super();
		this.vacationTask = vacationTask;
		this.hoursOfLeaveTask = hoursOfLeaveTask;
	}

	public WorkTask getVacationTask() {
		return vacationTask;
	}

	public WorkTask getHoursOfLeaveTask() {
		return hoursOfLeaveTask;
	}

}
