package it.palex.attendanceManagement.data.dto.security;

import java.util.Date;

import it.palex.attendanceManagement.library.rest.dtos.DTO;


/**
 * @author Alessandro Pagliaro
 *
 */
public class SuccessfullyLoginLogsDTO implements DTO {

	private static final long serialVersionUID = -8546964606992921671L;
	
	private String ip;
	private String userAgent;
	private Date loginDate;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	
	@Override
	public String toString() {
		return "SuccessfullyLoginLogsDTO [ip=" + ip + ", userAgent=" + userAgent + ", loginDate=" + loginDate + "]";
	}
	
}


