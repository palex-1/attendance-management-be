package it.palex.attendanceManagement.data.dto.documents;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

/**
 * @author Alessandro Pagliaro
 *
 */
public class TipologiaDocumentoDTO  implements DTO {

	private static final long serialVersionUID = 5095852206082893641L;

	private Integer id;
	private String nome;
	private CategoriaDocumentoDTO categoria;
	
	
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
	
	public CategoriaDocumentoDTO getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaDocumentoDTO categoria) {
		this.categoria = categoria;
	}
	
	@Override
	public String toString() {
		return "TipologiaDocumentoDTO [id=" + id + ", nome=" + nome + ", categoria=" + categoria + "]";
	}
	
}
