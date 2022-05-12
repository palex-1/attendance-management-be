package it.palex.attendanceManagement.auth.dto;

public class UpdateUserProfileSecondaryInfoDTO {

	private Integer userProfileId;
	private Integer levelId;
	private Integer companyId;
	private Integer workDayHours;
	private Double vacationDays;
	private Double leaveHours;
	private String employmentOffice;
	private Double hourlyCost;
	
	public Integer getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Integer userProfileId) {
		this.userProfileId = userProfileId;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getWorkDayHours() {
		return workDayHours;
	}

	public void setWorkDayHours(Integer workDayHours) {
		this.workDayHours = workDayHours;
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

	public String getEmploymentOffice() {
		return employmentOffice;
	}

	public void setEmploymentOffice(String employmentOffice) {
		this.employmentOffice = employmentOffice;
	}

	public Double getHourlyCost() {
		return hourlyCost;
	}

	public void setHourlyCost(Double hourlyCost) {
		this.hourlyCost = hourlyCost;
	}

	

	
}
