package it.palex.attendanceManagement.data.dto.documents;

import java.util.Date;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class TicketDownloadDTO implements DTO {

	private static final long serialVersionUID = 3364131951771379295L;
	
	private String tokenDownload;
    private Date creationDate;
    private Date expirationDate;
    
	public String getTokenDownload() {
		return tokenDownload;
	}
	public void setTokenDownload(String tokenDownload) {
		this.tokenDownload = tokenDownload;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	@Override
	public String toString() {
		return "TicketDownloadDTO [tokenDownload=" + tokenDownload + ", creationDate=" + creationDate
				+ ", expirationDate=" + expirationDate + "]";
	}

}
