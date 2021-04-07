package it.palex.attendanceManagement.data.dto.tasks;

import java.util.Date;

import it.palex.attendanceManagement.library.rest.dtos.DTO; 

/**
 * @author Alessandro Pagliaro
 *
 */
public class IncaricoInDTO implements DTO {

	private static final long serialVersionUID = -4621117760636012056L;
	
	private String codiceIncarico;
	private String descrizioneIncarico;
	private String pivaCliente;
	private Boolean fatturabile;
	private Date dataAttivazione;
	private Date dataDisattivazione;
	
	
	public String getCodiceIncarico() {
		return codiceIncarico;
	}
	public void setCodiceIncarico(String codiceIncarico) {
		this.codiceIncarico = null;
		if(codiceIncarico!=null) {
			this.codiceIncarico = codiceIncarico.trim();
		}
	}
	
	public String getDescrizioneIncarico() {
		return descrizioneIncarico;
	}
	public void setDescrizioneIncarico(String descrizioneIncarico) {
		this.descrizioneIncarico = null;
		if(descrizioneIncarico!=null) {
			this.descrizioneIncarico = descrizioneIncarico.trim();
		}
	}
	
	public String getPivaCliente() {
		return pivaCliente;
	}
	public void setPivaCliente(String pivaCliente) {
		this.pivaCliente = null;
		if(pivaCliente!=null) {
			this.pivaCliente = pivaCliente.trim();
		}
	}
	
	public Boolean getFatturabile() {
		return fatturabile;
	}
	public void setFatturabile(Boolean fatturabile) {
		this.fatturabile = fatturabile;
	}
	public Date getDataAttivazione() {
		return dataAttivazione;
	}
	public void setDataAttivazione(Date dataAttivazione) {
		this.dataAttivazione = dataAttivazione;
	}
	
	public Date getDataDisattivazione() {
		return dataDisattivazione;
	}
	public void setDataDisattivazione(Date dataDisattivazione) {
		this.dataDisattivazione = dataDisattivazione;
	}
	
	@Override
	public String toString() {
		return "IncaricoInDTO [codiceIncarico=" + codiceIncarico + ", descrizioneIncarico=" + descrizioneIncarico
				+ ", pivaCliente=" + pivaCliente + ", fatturabile=" + fatturabile + ", dataAttivazione="
				+ dataAttivazione + ", dataDisattivazione=" + dataDisattivazione + "]";
	}
	
}
