package it.palex.attendanceManagement.core.dtos.turnstile;

import java.util.Map;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class NameSurnameIdUsersDTO implements DTO {
	
	private static final long serialVersionUID = -5874806231129366704L;
	
	public Map<Integer, String> userNameIdMap;

	
	public Map<Integer, String> getUserNameIdMap() {
		return userNameIdMap;
	}

	public void setUserNameIdMap(Map<Integer, String> userNameIdMap) {
		this.userNameIdMap = userNameIdMap;
	}
	
	
}
