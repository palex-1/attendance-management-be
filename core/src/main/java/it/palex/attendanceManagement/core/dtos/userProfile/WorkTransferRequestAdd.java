package it.palex.attendanceManagement.core.dtos.userProfile;

import java.util.Date;

public class WorkTransferRequestAdd {

	private Date day;
	private String type;

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
