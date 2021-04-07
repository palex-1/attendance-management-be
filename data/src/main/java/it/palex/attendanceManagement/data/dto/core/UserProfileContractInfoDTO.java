package it.palex.attendanceManagement.data.dto.core;

import java.util.Date;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class UserProfileContractInfoDTO implements DTO {

	private static final long serialVersionUID = 8215385963448333692L;
	
	private Integer id;
	private Integer workDayHours;
	private UserLevelDTO level;
	private Double vacationDays;
	private Double leaveHours;
	private Double bankHours;
	private Date hiringDate;
	private String employmentOffice;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWorkDayHours() {
		return workDayHours;
	}

	public void setWorkDayHours(Integer workDayHours) {
		this.workDayHours = workDayHours;
	}

	public UserLevelDTO getLevel() {
		return level;
	}

	public void setLevel(UserLevelDTO level) {
		this.level = level;
	}

	public Double getVacationDays() {
		return vacationDays;
	}

	public void setVacationDays(Double vacationDays) {
		this.vacationDays = vacationDays;
	}

	public Double getLeaveHours() {
		return leaveHours;
	}

	public void setLeaveHours(Double leaveHours) {
		this.leaveHours = leaveHours;
	}

	public Double getBankHours() {
		return bankHours;
	}

	public void setBankHours(Double bankHours) {
		this.bankHours = bankHours;
	}

	public Date getHiringDate() {
		return hiringDate;
	}

	public void setHiringDate(Date hiringDate) {
		this.hiringDate = hiringDate;
	}

	public String getEmploymentOffice() {
		return employmentOffice;
	}

	public void setEmploymentOffice(String employmentOffice) {
		this.employmentOffice = employmentOffice;
	}


}
