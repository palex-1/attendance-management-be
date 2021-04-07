package it.palex.attendanceManagement.core.dtos.tasks;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class CompletedTaskUpdateDTO implements DTO {

	private static final long serialVersionUID = 6497241629576293162L;
	
	private Long id;
	private Short workedHours;
	private Boolean smartworked;
	private Integer userProfileId;

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

	public Boolean getSmartworked() {
		return smartworked;
	}

	public void setSmartworked(Boolean smartworked) {
		this.smartworked = smartworked;
	}

	public Integer getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Integer userProfileId) {
		this.userProfileId = userProfileId;
	}

}
