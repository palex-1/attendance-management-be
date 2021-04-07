package it.palex.attendanceManagement.core.dtos.personalDocuments;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class PaycheckAddDTO implements DTO {

	private static final long serialVersionUID = 1222790049962776820L;
	
	private Integer month;
	private Integer year;
	private Integer userProfileId;
	private String title;
	private Boolean checkFiscalCode;
	private Boolean forceAdd;

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Integer userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getCheckFiscalCode() {
		return checkFiscalCode;
	}

	public void setCheckFiscalCode(Boolean checkFiscalCode) {
		this.checkFiscalCode = checkFiscalCode;
	}

	public Boolean getForceAdd() {
		return forceAdd;
	}

	public void setForceAdd(Boolean forceAdd) {
		this.forceAdd = forceAdd;
	}
	
}
