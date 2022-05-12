package it.palex.attendanceManagement.data.pseudoentities;

public class WorkTaskSummaryPseudoEntity {

	private Integer userId;
	private String userName;
	private String userSurname;
	private String userEmail;
	private String userPhoneNumber;

	private Long workedHours;
	private Double sumOfCost;

	private Integer companyId;
	private String companyName;
	private String companyDescription;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSurname() {
		return userSurname;
	}

	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}

	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}

	public Long getWorkedHours() {
		return workedHours;
	}

	public void setWorkedHours(Long workedHours) {
		this.workedHours = workedHours;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDescription() {
		return companyDescription;
	}

	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}

	public Double getSumOfCost() {
		return sumOfCost;
	}

	public void setSumOfCost(Double sumOfCost) {
		this.sumOfCost = sumOfCost;
	}
}
