package it.palex.attendanceManagement.auth.dto;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class ConfirmPasswordDTO implements DTO {

	private static final long serialVersionUID = -5237267220318965306L;
	
	private String token;
	private String password;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
