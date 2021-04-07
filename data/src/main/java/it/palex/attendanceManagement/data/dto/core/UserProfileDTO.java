package it.palex.attendanceManagement.data.dto.core;

import java.util.Date;

import it.palex.attendanceManagement.data.dto.auth.PermissionGroupLabelDTO;
import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class UserProfileDTO implements DTO {

	private static final long serialVersionUID = -3792984458158642861L;

	private Integer id;
	public Boolean accountLocked;
	public Boolean accountDisabled;
	
	private String name;
	private String surname;
	private Date birthDate;
	private String sex;
	private String email;
	private String cf;
	private String phoneNumber;
	private Date employmentDate;
	private String userProfileImageDownloadToken;
	private CompanyDTO company;
	private PermissionGroupLabelDTO permissionGroupLabel;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CompanyDTO getCompany() {
		return company;
	}

	public void setCompany(CompanyDTO company) {
		this.company = company;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getEmploymentDate() {
		return employmentDate;
	}

	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserProfileImageDownloadToken() {
		return userProfileImageDownloadToken;
	}

	public void setUserProfileImageDownloadToken(String userProfileImageDownloadToken) {
		this.userProfileImageDownloadToken = userProfileImageDownloadToken;
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public PermissionGroupLabelDTO getPermissionGroupLabel() {
		return permissionGroupLabel;
	}

	public void setPermissionGroupLabel(PermissionGroupLabelDTO permissionGroupLabel) {
		this.permissionGroupLabel = permissionGroupLabel;
	}

	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public Boolean getAccountDisabled() {
		return accountDisabled;
	}

	public void setAccountDisabled(Boolean accountDisabled) {
		this.accountDisabled = accountDisabled;
	}
	


}
