package it.palex.attendanceManagement.data.dto.core;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class AddFcmUserTokenDTO implements DTO {

	private static final long serialVersionUID = -8876748963952958849L;

	private String provider;
	private String deviceId;
	private String token;
	
	
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
    
	
}

