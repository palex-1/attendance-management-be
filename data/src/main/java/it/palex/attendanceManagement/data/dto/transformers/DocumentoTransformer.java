package it.palex.attendanceManagement.data.dto.transformers;

import it.palex.attendanceManagement.data.dto.documents.DocumentoDTO;
import it.palex.attendanceManagement.data.entities.Document;

public class DocumentoTransformer {

	
	public static DocumentoDTO mapToDTO(Document doc) {
		if(doc==null) {
			return null;
		}
		DocumentoDTO res = new DocumentoDTO();
		res.setDescrizione(doc.getDescription());
		res.setId(doc.getId());
		res.setNome(doc.getFullFileName());
		
		return res;
	}
	
	
}
