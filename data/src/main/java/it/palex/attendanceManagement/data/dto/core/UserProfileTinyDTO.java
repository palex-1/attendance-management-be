package it.palex.attendanceManagement.data.dto.core;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class UserProfileTinyDTO implements DTO {

	private static final long serialVersionUID = -7980575477704107816L;
	
	private Integer id;
	private String name;
    private String surname;
    
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
}