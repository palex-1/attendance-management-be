package it.palex.attendanceManagement.core.dtos.tasks;

import java.util.Date;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class CompletedTaskAddDTO implements DTO {

	private static final long serialVersionUID = -3004512011709422832L;
	
	private Short workedHours;
	private Date day;
	private Boolean smartworked;
	private String taskCode;
	private Integer userProfileId;

	public Short getWorkedHours() {
		return workedHours;
	}

	public void setWorkedHours(Short workedHours) {
		this.workedHours = workedHours;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Boolean getSmartworked() {
		return smartworked;
	}

	public void setSmartworked(Boolean smartworked) {
		this.smartworked = smartworked;
	}
	
	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public Integer getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Integer userProfileId) {
		this.userProfileId = userProfileId;
	}

}
