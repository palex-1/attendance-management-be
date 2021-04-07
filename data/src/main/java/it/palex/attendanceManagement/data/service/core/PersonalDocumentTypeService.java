package it.palex.attendanceManagement.data.service.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.data.entities.core.PersonalDocumentType;
import it.palex.attendanceManagement.data.repository.core.PersonalDocumentTypeRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;
import it.palex.attendanceManagement.library.utils.StringUtility;

@Service
public class PersonalDocumentTypeService implements BasicGenericService {

	@Autowired
	private PersonalDocumentTypeRepository personalDocumentTypeRepository;
	
	
	public List<PersonalDocumentType> findAll(){
		return this.personalDocumentTypeRepository.findAll();
	}


	public PersonalDocumentType findById(Integer documentTypeId) {
		if(documentTypeId==null) {
			return null;
		}
		Optional<PersonalDocumentType> opt = this.personalDocumentTypeRepository.findById(documentTypeId);
		
		return this.getFromOptional(opt);
	}


	public List<String> buildSupportedExtensionsLowercase(PersonalDocumentType type) {
		if(type==null) {
			throw new NullPointerException();
		}
		List<String> res = new ArrayList<>();
		String extensionsCsv = type.getExtensionsSupported();
		
		if(!StringUtility.isEmptyOrWhitespace(extensionsCsv)) {
			extensionsCsv = extensionsCsv.trim();
			String[] exts = extensionsCsv.split(",");
			
			for(int i=0; i<exts.length; i++) {
				String ext = exts[i].trim().toLowerCase();
				if(!StringUtility.isEmptyOrWhitespace(ext)) {
					res.add(ext);
				}
			}
		}
		
		return res;
	}
	
	
}
