package it.palex.attendanceManagement.data.dto.tasks;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class ComponentCreationDTO implements DTO {

	private static final long serialVersionUID = 2623397112273636900L;
	
	private Integer idImpiegato;
	private String ruolo;
	
	
	public Integer getIdImpiegato() {
		return idImpiegato;
	}
	public void setIdImpiegato(Integer idImpiegato) {
		this.idImpiegato = idImpiegato;
	}
	
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	
	@Override
	public String toString() {
		return "ComponentCreationDTO [idImpiegato=" + idImpiegato + ", ruolo=" + ruolo + "]";
	}
	
	
	
}
