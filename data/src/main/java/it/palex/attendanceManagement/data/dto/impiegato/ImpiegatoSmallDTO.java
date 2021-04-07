package it.palex.attendanceManagement.data.dto.impiegato;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

/**
 * @author Alessandro Pagliaro
 *
 */
public class ImpiegatoSmallDTO implements DTO{

	private static final long serialVersionUID = -3730564113592861918L;

	private Integer id;
	private String nome;
	private String cognome;
	private String email;
	private String numero;
	private String nomeAzienda;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getNomeAzienda() {
		return nomeAzienda;
	}
	public void setNomeAzienda(String nomeAzienda) {
		this.nomeAzienda = nomeAzienda;
	}
	
	@Override
	public String toString() {
		return "ImpiegatoSmallDTO [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email
				+ ", numero=" + numero + ", nomeAzienda=" + nomeAzienda + "]";
	}
	
	
}
