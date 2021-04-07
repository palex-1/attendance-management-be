package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.CompanyDTO;
import it.palex.attendanceManagement.data.entities.Company;

public class CompanyTransformer {

	
	public static CompanyDTO mapToDTO(Company company) {
		if(company==null) {
			return null;
		}
		CompanyDTO res = new CompanyDTO();
		res.setId(company.getId());
		res.setName(company.getName());
		res.setDescription(company.getDescription());
		res.setIsRoot(company.getIsRoot());
		
		return res;
	}
	
	public static List<CompanyDTO> mapToDTO(List<Company> companies) {
		if(companies==null) {
			return null;
		}
		
		
		List<CompanyDTO> res = new ArrayList<>(companies.size());
		
		for (Company company : companies) {
			res.add(mapToDTO(company));
		}
		
		return res;
	}
	
}
