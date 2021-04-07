package it.palex.attendanceManagement.data.dto.tasks;

import it.palex.attendanceManagement.data.dto.impiegato.ImpiegatoSmallDTO;
import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class ComponenteTeamOutDTO implements DTO{

	private static final long serialVersionUID = -4055805194361290960L;
	
	private ImpiegatoSmallDTO impiegato;
	private String ruolo;
	
	
	public ImpiegatoSmallDTO getImpiegato() {
		return impiegato;
	}
	public void setImpiegato(ImpiegatoSmallDTO impiegato) {
		this.impiegato = impiegato;
	}
	
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	
	@Override
	public String toString() {
		return "ComponenteTeamOutDTO [impiegato=" + impiegato + ", ruolo=" + ruolo + "]";
	}
	
}
