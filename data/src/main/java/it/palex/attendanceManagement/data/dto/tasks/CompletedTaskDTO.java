package it.palex.attendanceManagement.data.dto.tasks;

import java.util.Date;

import it.palex.attendanceManagement.data.dto.core.UserProfileTinyDTO;
import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class CompletedTaskDTO implements DTO {

	private static final long serialVersionUID = -6773363761495107012L;
	
	private Long id;
	private Short workedHours;
	private Date day;
	private Boolean smartworked;
	private Boolean editable;
	private WorkTaskDTO taskCode;
	private UserProfileTinyDTO userProfile;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public WorkTaskDTO getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(WorkTaskDTO taskCode) {
		this.taskCode = taskCode;
	}

	public UserProfileTinyDTO getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfileTinyDTO userProfile) {
		this.userProfile = userProfile;
	}

}
