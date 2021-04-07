package it.palex.attendanceManagement.data.dto.sede;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

/**
 * @author Alessandro Pagliaro
 *
 */
public class SedeLavorativaInDTO implements DTO{

	private static final long serialVersionUID = 1880588879644231674L;
	
	private String nomeSede;
    private String via;
    private String citta;
    private String provincia;
    private String nazione;
    private String cap;
    private String tipoSede;
    
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
		return "SedeLavorativaInDTO [nomeSede=" + nomeSede + ", via=" + via + ", citta="
				+ citta + ", provincia=" + provincia + ", nazione=" + nazione + ", cap=" + cap + ", tipoSede="
				+ tipoSede + "]";
	}
        
}
