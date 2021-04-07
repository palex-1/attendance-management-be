package it.palex.attendanceManagement.data.dto.documents;

import java.util.Date;

import it.palex.attendanceManagement.data.dto.impiegato.ImpiegatoTinyDTO;
import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class CedolinoDTO implements DTO{

	private static final long serialVersionUID = -9003680685758566009L;

    private Long id;
    private Date dataInserimento;
    private String titolo;
    private Date dataInvioEmail;
    private Integer annoRiferimento;
    private Integer meseRiferimento;
    
    private ImpiegatoTinyDTO impiegato;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public Date getDataInvioEmail() {
		return dataInvioEmail;
	}
	public void setDataInvioEmail(Date dataInvioEmail) {
		this.dataInvioEmail = dataInvioEmail;
	}
	
	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}
	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}
	
	public Integer getMeseRiferimento() {
		return meseRiferimento;
	}
	public void setMeseRiferimento(Integer meseRiferimento) {
		this.meseRiferimento = meseRiferimento;
	}
	
	public ImpiegatoTinyDTO getImpiegato() {
		return impiegato;
	}
	public void setImpiegato(ImpiegatoTinyDTO impiegato) {
		this.impiegato = impiegato;
	}
	
	
	@Override
	public String toString() {
		return "CedolinoDTO [id=" + id + ", dataInserimento=" + dataInserimento + ", titolo=" + titolo
				+ ", dataInvioEmail=" + dataInvioEmail + ", annoRiferimento=" + annoRiferimento + ", meseRiferimento="
				+ meseRiferimento + ", impiegato=" + impiegato + "]";
	}
    
}
