package it.palex.attendanceManagement.data.dto.impiegato;

import java.util.Date;

public class PaycheckDTO {
	
	private Long id;
	private Date sendEmailDate;
	private Integer year;
	private String title;
	private Integer month;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSendEmailDate() {
		return sendEmailDate;
	}

	public void setSendEmailDate(Date sendEmailDate) {
		this.sendEmailDate = sendEmailDate;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

}
