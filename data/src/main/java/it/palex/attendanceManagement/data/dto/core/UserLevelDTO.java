package it.palex.attendanceManagement.data.dto.core;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class UserLevelDTO implements DTO {

	private static final long serialVersionUID = 8212402557940279665L;

	private Integer id;
	private String level;
	private Double monthlyVacationDays;
	private Double monthlyLeaveHours;
	private Boolean bankHourEnabled;
	private Boolean extraWorkPaid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Double getMonthlyVacationDays() {
		return monthlyVacationDays;
	}

	public void setMonthlyVacationDays(Double monthlyVacationDays) {
		this.monthlyVacationDays = monthlyVacationDays;
	}

	public Double getMonthlyLeaveHours() {
		return monthlyLeaveHours;
	}

	public void setMonthlyLeaveHours(Double monthlyLeaveHours) {
		this.monthlyLeaveHours = monthlyLeaveHours;
	}

	public Boolean getBankHourEnabled() {
		return bankHourEnabled;
	}

	public void setBankHourEnabled(Boolean bankHourEnabled) {
		this.bankHourEnabled = bankHourEnabled;
	}

	public Boolean getExtraWorkPaid() {
		return extraWorkPaid;
	}

	public void setExtraWorkPaid(Boolean extraWorkPaid) {
		this.extraWorkPaid = extraWorkPaid;
	}

}
