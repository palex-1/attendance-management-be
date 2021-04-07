package it.palex.attendanceManagement.auth.dto;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class ChangePasswordDTO  implements DTO {

	private static final long serialVersionUID = -6504708539012199654L;

	private String oldPassword;
	private String newPassword;
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	@Override
	public String toString() {
		return "ChangePasswordDTO [oldPassword=" + oldPassword + ", newPassword=" + newPassword + "]";
	}	
	
}
