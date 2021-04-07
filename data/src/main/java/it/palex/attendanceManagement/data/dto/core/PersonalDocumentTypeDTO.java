package it.palex.attendanceManagement.data.dto.core;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class PersonalDocumentTypeDTO implements DTO {
	
	private static final long serialVersionUID = -2792002120438505256L;
	
	private Integer id;
    private String type;
    private String supportedExtensions;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSupportedExtensions() {
		return supportedExtensions;
	}
	public void setSupportedExtensions(String supportedExtensions) {
		this.supportedExtensions = supportedExtensions;
	}
    
    
}
