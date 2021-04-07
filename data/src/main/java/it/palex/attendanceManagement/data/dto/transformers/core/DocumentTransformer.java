package it.palex.attendanceManagement.data.dto.transformers.core;

import it.palex.attendanceManagement.data.dto.core.DocumentDTO;
import it.palex.attendanceManagement.data.entities.Document;

public class DocumentTransformer {

	public static DocumentDTO mapToDTO(Document doc) {
		if(doc==null) {
			return null;
		}
		DocumentDTO res = new DocumentDTO();
		res.setFileName(doc.getFullFileName());
		res.setId(doc.getId());
		
		return res;
	}
}
