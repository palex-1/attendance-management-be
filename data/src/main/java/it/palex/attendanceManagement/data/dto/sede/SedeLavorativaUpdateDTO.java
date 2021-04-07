package it.palex.attendanceManagement.data.dto.sede;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class SedeLavorativaUpdateDTO implements DTO{

	private static final long serialVersionUID = -2650780610877101187L;
	
	private Integer id;
    private String nomeSede;
    private String via;
    private String civico;
    private String citta;
    private String provincia;
    private String nazione;
    private String cap;
    private String tipoSede;
    
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNomeSede() {
		return nomeSede;
	}
	public void setNomeSede(String nomeSede) {
		this.nomeSede = null;
		if(nomeSede!=null) {
			this.nomeSede = nomeSede.trim();
		}
	}
	
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = null;
		if(via!=null) {
			this.via = via.trim();
		}
	}
	
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = null;
		if(civico!=null) {
			this.civico = civico.trim();
		}		
	}
	
	public String getCitta() {
		return citta;
	}
	public void setCitta(String citta) {
		this.citta = null;
		if(citta!=null) {
			this.citta = citta.trim();
		}
	}
	
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = null;
		if(provincia!=null) {
			this.provincia = provincia.trim();
		}
	}
	
	public String getNazione() {
		return nazione;
	}
	public void setNazione(String nazione) {
		this.nazione = null;
		if(nazione!=null) {
			this.nazione = nazione.trim();
		}
	}
	
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = null;
		if(cap!=null) {
			this.cap = cap.trim();
		}
	}
	
	public String getTipoSede() {
		return tipoSede;
	}
	public void setTipoSede(String tipoSede) {
		this.tipoSede = null;
		if(tipoSede!=null) {
			this.tipoSede = tipoSede.trim();
		}
	}
	
	
	@Override
	public String toString() {
		return "SedeLavorativaUpdateDTO [id=" + id + ", nomeSede=" + nomeSede + ", via=" + via + ", civico="
				+ civico + ", citta=" + citta + ", provincia=" + provincia + ", nazione=" + nazione + ", cap=" + cap
				+ ", tipoSede=" + tipoSede + "]";
	}
	
	
}
