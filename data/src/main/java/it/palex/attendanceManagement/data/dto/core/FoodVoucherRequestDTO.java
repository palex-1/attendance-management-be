package it.palex.attendanceManagement.data.dto.core;

import java.util.Date;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class FoodVoucherRequestDTO implements DTO {

	private static final long serialVersionUID = 2098294743889786254L;
	
	private Long id;
	private Date day;
	private Boolean editable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

}
