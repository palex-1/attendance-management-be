package it.palex.attendanceManagement.data.dto.sede;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

/**
 * @author Alessandro Pagliaro
 *
 */
public class SedeLavorativaOutDTO implements DTO{

	private static final long serialVersionUID = 5044781965159602429L;
	
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
		this.nomeSede = nomeSede;
	}
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getCitta() {
		return citta;
	}
	public void setCitta(String citta) {
		this.citta = citta;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getNazione() {
		return nazione;
	}
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getTipoSede() {
		return tipoSede;
	}
	public void setTipoSede(String tipoSede) {
		this.tipoSede = tipoSede;
	}
	
	
	@Override
	public String toString() {
		return "SedeLavorativaOutDTO [id=" + id + ", nomeSede=" + nomeSede + ", via=" + via + ", civico=" + civico
				+ ", citta=" + citta + ", provincia=" + provincia + ", nazione=" + nazione + ", cap=" + cap
				+ ", tipoSede=" + tipoSede + "]";
	}
    
}
