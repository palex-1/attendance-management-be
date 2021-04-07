package it.palex.attendanceManagement.auth.dto;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class AuthenticationDTO implements DTO {

	private static final long serialVersionUID = -1409509736592966229L;

	private String username; 
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

