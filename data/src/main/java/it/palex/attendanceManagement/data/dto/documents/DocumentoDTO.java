package it.palex.attendanceManagement.data.dto.documents;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class DocumentoDTO implements DTO {

	private static final long serialVersionUID = 3685052253216676345L;

	private Long id;
    private String nome;
    private String descrizione;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	@Override
	public String toString() {
		return "DocumentoDTO [id=" + id + ", nome=" + nome + ", descrizione=" + descrizione + "]";
	}
	
}
