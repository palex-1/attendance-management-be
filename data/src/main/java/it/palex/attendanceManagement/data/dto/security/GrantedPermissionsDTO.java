package it.palex.attendanceManagement.data.dto.security;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public class GrantedPermissionsDTO implements DTO {

	private static final long serialVersionUID = 4414824509767502038L;

	private Boolean readPermission;
	private Boolean updatePermission;
	private Boolean creationPermission;
	private Boolean deletePermission;
	
	
	public Boolean getReadPermission() {
		return readPermission;
	}
	
	public void setReadPermission(Boolean readPermission) {
		this.readPermission = readPermission;
	}
	
	
	public Boolean getUpdatePermission() {
		return updatePermission;
	}
	public void setUpdatePermission(Boolean updatePermission) {
		this.updatePermission = updatePermission;
	}
	
	public Boolean getCreationPermission() {
		return creationPermission;
	}
	public void setCreationPermission(Boolean creationPermission) {
		this.creationPermission = creationPermission;
	}
	
	
	public Boolean getDeletePermission() {
		return deletePermission;
	}
	public void setDeletePermission(Boolean deletePermission) {
		this.deletePermission = deletePermission;
	}
	
	
	@Override
	public String toString() {
		return "GrantedPermissionsDTO [readPermission=" + readPermission + ", updatePermission=" + updatePermission
				+ ", creationPermission=" + creationPermission + ", deletePermission=" + deletePermission + "]";
	}
	
}
