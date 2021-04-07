package it.palex.attendanceManagement.core.dtos.tasks;

import java.util.Date;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class FoodVoucherRequestAdd implements DTO {

	private static final long serialVersionUID = -7573621829971603448L;
	
	private Date day;

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}
	
}
