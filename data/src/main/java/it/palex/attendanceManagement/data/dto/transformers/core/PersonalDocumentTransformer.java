package it.palex.attendanceManagement.data.dto.transformers.core;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.PersonalDocumentDTO;
import it.palex.attendanceManagement.data.entities.core.PersonalDocument;

public class PersonalDocumentTransformer {

	
	public static PersonalDocumentDTO mapToDTO(PersonalDocument doc) {
		if(doc==null) {
			return null;
		}
		PersonalDocumentDTO res = new PersonalDocumentDTO();
		res.setId(doc.getId());
		res.setUploadDate(doc.getUploadDate());
		res.setPersonalDocumentType(PersonalDocumentTypeTransformer.mapToDTO(doc.getPersonalDocumentType()));
		res.setEditable(doc.getEditable());
		
		return res;
	}
	
	public static List<PersonalDocumentDTO> mapToDTO(List<PersonalDocument> list){
		if(list==null) {
			return null;
		}
		
		List<PersonalDocumentDTO> res = new ArrayList<>();
		
		for (PersonalDocument personalDocument : list) {
			res.add(mapToDTO(personalDocument));
		}
		
		return res;
	}
}
