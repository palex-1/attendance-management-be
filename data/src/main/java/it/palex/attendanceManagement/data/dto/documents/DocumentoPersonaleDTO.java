package it.palex.attendanceManagement.data.dto.documents;

import java.util.Date;

import it.palex.attendanceManagement.data.dto.impiegato.ImpiegatoTinyDTO;

public class DocumentoPersonaleDTO {

	private Long id;
    private Date dataInserimento;
    private DocumentoDTO documento;
    private ImpiegatoTinyDTO impiegato;
    private TipologiaDocumentoDTO tipologiaDocumento;
    
    
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
	
	public DocumentoDTO getDocumento() {
		return documento;
	}
	public void setDocumento(DocumentoDTO documento) {
		this.documento = documento;
	}
	
	public ImpiegatoTinyDTO getImpiegato() {
		return impiegato;
	}
	public void setImpiegato(ImpiegatoTinyDTO impiegato) {
		this.impiegato = impiegato;
	}
	
	public TipologiaDocumentoDTO getTipologiaDocumento() {
		return tipologiaDocumento;
	}
	public void setTipologiaDocumento(TipologiaDocumentoDTO tipologiaDocumento) {
		this.tipologiaDocumento = tipologiaDocumento;
	}
	
	
	@Override
	public String toString() {
		return "DocumentoPersonaleDTO [id=" + id + ", dataInserimento=" + dataInserimento + ", documento=" + documento
				+ ", impiegato=" + impiegato + ", tipologiaDocumento=" + tipologiaDocumento + "]";
	}
    
}
