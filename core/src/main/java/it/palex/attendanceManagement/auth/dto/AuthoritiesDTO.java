package it.palex.attendanceManagement.auth.dto;

import java.util.List;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

/**
 * @author Alessandro Pagliaro
 *
 */
public class AuthoritiesDTO implements DTO{

	private static final long serialVersionUID = 5152811916869892227L;

	private List<String> authorities;

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return "AuthoritiesDTO [authorities=" + authorities + "]";
	}
		
}
