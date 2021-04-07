package it.palex.attendanceManagement.library.rest.dtos;

import java.util.Date;

/**
 * @author Alessandro Pagliaro
 *
 */
public class FeLoggingDTO implements DTO {
	
	private static final long serialVersionUID = 5378749462600385854L;
	
	private Date date;
	private String message;
	private String frontendVersion;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getFrontendVersion() {
		return frontendVersion;
	}
	public void setFrontendVersion(String frontendVersion) {
		this.frontendVersion = frontendVersion;
	}
	
	@Override
	public String toString() {
		return "FeLoggingDTO [date=" + date + ", message=" + message + ", frontendVersion=" + frontendVersion + "]";
	}
	
}

