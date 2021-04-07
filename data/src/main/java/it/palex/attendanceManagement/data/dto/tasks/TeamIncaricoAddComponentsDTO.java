package it.palex.attendanceManagement.data.dto.tasks;

import java.util.List;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class TeamIncaricoAddComponentsDTO implements DTO {

	private static final long serialVersionUID = 1394452283218027639L;

	private List<ComponentCreationDTO> componentsToCreate;

	public List<ComponentCreationDTO> getComponentsToCreate() {
		return componentsToCreate;
	}

	public void setComponentsToCreate(List<ComponentCreationDTO> componentsToCreate) {
		this.componentsToCreate = componentsToCreate;
	}

	@Override
	public String toString() {
		return "TeamIncaricoAddComponentsDTO [componentsToCreate=" + componentsToCreate + "]";
	}
	
}
