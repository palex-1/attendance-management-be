package it.palex.attendanceManagement.data.dto.documents;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

/**
 * @author Alessandro Pagliaro
 *
 */
public class CategoriaDocumentoDTO  implements DTO {

	private static final long serialVersionUID = 2655124933232683687L;

	private String nome;
	private Integer id;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "CategoriaDocumentoDTO [nome=" + nome + ", id=" + id + "]";
	}
	
}
