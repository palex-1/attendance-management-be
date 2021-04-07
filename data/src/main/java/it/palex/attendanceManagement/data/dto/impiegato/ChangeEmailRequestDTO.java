package it.palex.attendanceManagement.data.dto.impiegato;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

/**
 * @author Alessandro Pagliaro
 *
 */
public class ChangeEmailRequestDTO implements DTO{

	private static final long serialVersionUID = 1413095252882061491L;
	
	private String email;
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "ChangeEmailRequestDTO [email=" + email + ", password=" + password + "]";
	}
		
	
}
