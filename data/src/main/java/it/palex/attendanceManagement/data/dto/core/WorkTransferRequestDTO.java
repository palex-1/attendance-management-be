package it.palex.attendanceManagement.data.dto.core;

import java.util.Date;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class WorkTransferRequestDTO implements DTO {

	private static final long serialVersionUID = 3763734629192962160L;

	private Long id;
	private Date day;
	private String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
