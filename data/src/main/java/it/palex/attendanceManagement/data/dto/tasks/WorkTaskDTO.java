package it.palex.attendanceManagement.data.dto.tasks;

import java.util.Date;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

/**
 * @author Alessandro Pagliaro
 *
 */
public class WorkTaskDTO implements DTO {

	private static final long serialVersionUID = 2402089334335397227L;

	private Long id;
	private String taskCode;
	private String taskDescription;
	private String clientVat;
	private Boolean billable;
	private Date activationDate;
	private Date deactivationDate;
	private Boolean isEnabledForAllUsers;
	private Boolean isAbsenceTask;
	
	private Boolean currentUserCanSeeDetails;

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getClientVat() {
		return clientVat;
	}

	public void setClientVat(String clientVat) {
		this.clientVat = clientVat;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Date getDeactivationDate() {
		return deactivationDate;
	}

	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public Boolean getIsEnabledForAllUsers() {
		return isEnabledForAllUsers;
	}

	public void setIsEnabledForAllUsers(Boolean isEnabledForAllUsers) {
		this.isEnabledForAllUsers = isEnabledForAllUsers;
	}

	public Boolean getIsAbsenceTask() {
		return isAbsenceTask;
	}

	public void setIsAbsenceTask(Boolean isAbsenceTask) {
		this.isAbsenceTask = isAbsenceTask;
	}

	public Boolean getCurrentUserCanSeeDetails() {
		return currentUserCanSeeDetails;
	}

	public void setCurrentUserCanSeeDetails(Boolean currentUserCanSeeDetails) {
		this.currentUserCanSeeDetails = currentUserCanSeeDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
}
