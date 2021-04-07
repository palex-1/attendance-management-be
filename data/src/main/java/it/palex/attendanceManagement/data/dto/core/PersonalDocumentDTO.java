package it.palex.attendanceManagement.data.dto.core;

import java.util.Date;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class PersonalDocumentDTO implements DTO {

	private static final long serialVersionUID = 539034594025206590L;
	
	private Long id;
	private PersonalDocumentTypeDTO personalDocumentType;
	private Date uploadDate;
	private Boolean editable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PersonalDocumentTypeDTO getPersonalDocumentType() {
		return personalDocumentType;
	}

	public void setPersonalDocumentType(PersonalDocumentTypeDTO personalDocumentType) {
		this.personalDocumentType = personalDocumentType;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	
}
