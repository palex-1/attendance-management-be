package it.palex.attendanceManagement.core.dtos.personalDocuments;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class PersonalDocumentAddRequest implements DTO {
	
	private static final long serialVersionUID = -5083347909930976916L;
	
	private Integer userProfileId;
	private Integer documentTypeId;
	private Boolean editable;
	
	public Integer getDocumentTypeId() {
		return documentTypeId;
	}
	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	
	public Integer getUserProfileId() {
		return userProfileId;
	}
	public void setUserProfileId(Integer userProfileId) {
		this.userProfileId = userProfileId;
	}
	
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	
	
	
}
