package it.palex.attendanceManagement.data.dto.transformers.core;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.PersonalDocumentTypeDTO;
import it.palex.attendanceManagement.data.entities.core.PersonalDocumentType;

public class PersonalDocumentTypeTransformer {

	public static PersonalDocumentTypeDTO mapToDTO(PersonalDocumentType type) {
		if(type==null) {
			return null;
		}
		PersonalDocumentTypeDTO res = new PersonalDocumentTypeDTO();
		res.setId(type.getId());
		res.setType(type.getType());
		res.setSupportedExtensions(type.getExtensionsSupported());
		
		return res;
	}
	
	public static List<PersonalDocumentTypeDTO> mapToDTO(List<PersonalDocumentType> list){
		if(list==null) {
			return null;
		}
		
		List<PersonalDocumentTypeDTO> res = new ArrayList<>();
		
		for (PersonalDocumentType personalDocumentType : list) {
			res.add(mapToDTO(personalDocumentType));
		}
		
		return res;
	}
}
