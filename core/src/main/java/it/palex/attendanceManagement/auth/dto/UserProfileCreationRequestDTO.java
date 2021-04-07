package it.palex.attendanceManagement.auth.dto;

import java.util.Date;

public class UserProfileCreationRequestDTO {

	private String username;
	private String email;
	private String phoneNumber;
	private Integer permissionGroupLabelId;
	private String name;
	private String surname;
	private String sex;
	private String fiscalCode;
	private Date birthDate;
	private Date dateOfEmployment;
	private Integer levelId;
	private Double initialVacationDays;
	private Double initialLeaveHours;
	private Integer workDayHours;
	private Integer companyId;
	private String employmentOffice;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	
	public Double getInitialVacationDays() {
		return initialVacationDays;
	}

	public void setInitialVacationDays(Double initialVacationDays) {
		this.initialVacationDays = initialVacationDays;
	}

	public Double getInitialLeaveHours() {
		return initialLeaveHours;
	}

	public void setInitialLeaveHours(Double initialLeaveHours) {
		this.initialLeaveHours = initialLeaveHours;
	}

	public Integer getPermissionGroupLabelId() {
		return permissionGroupLabelId;
	}

	public void setPermissionGroupLabelId(Integer permissionGroupLabelId) {
		this.permissionGroupLabelId = permissionGroupLabelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getDateOfEmployment() {
		return dateOfEmployment;
	}

	public void setDateOfEmployment(Date dateOfEmployment) {
		this.dateOfEmployment = dateOfEmployment;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Integer getWorkDayHours() {
		return workDayHours;
	}

	public void setWorkDayHours(Integer workDayHours) {
		this.workDayHours = workDayHours;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getEmploymentOffice() {
		return employmentOffice;
	}

	public void setEmploymentOffice(String employmentOffice) {
		this.employmentOffice = employmentOffice;
	}

}
