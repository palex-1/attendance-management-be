package it.palex.attendanceManagement.data.service.core;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.Company;
import it.palex.attendanceManagement.data.entities.QCompany;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.CompanyRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 29 mag 2020
 */
@Service
public class CompanyService implements BasicGenericService {

	private final QCompany QC = QCompany.company;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	
	public Company saveOrUpdate(Company company) {
		if(company==null) {
			throw new NullPointerException();
		}
		if(!company.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase();
		}
		return this.companyRepository.save(company);
	}
	
	public Company findRootCompany() {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QC.isRoot.isTrue());
		
		return this.getFirstResultFromIterable(
					this.companyRepository.findAll(cond)
				);
	}
	
	public Company findById(Integer id) {
		if(id==null) {
			throw new NullPointerException("id is null");
		}
		Optional<Company> res = this.companyRepository.findById(id);
		
		return this.getFromOptional(res);
	}

	public Pair<List<Company>, Long> findAllAndCount(String name, String description, Pageable pageable){
		if(pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		
		if(name!=null) {
			cond.and(QC.name.containsIgnoreCase(name));
		}
		
		if(description!=null) {
			cond.and(QC.description.containsIgnoreCase(description));
		}
		
		long count = this.companyRepository.count(cond);
		List<Company> list = this.iterableToList(this.companyRepository.findAll(cond, pageable));
				
		return Pair.of(list, count);
	}
		
	public List<Company> findAll(Sort sort) {
		return this.companyRepository.findAll(sort);
	}

	public boolean checkCompanyNameExistance(String name) {
		if(name==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QC.name.equalsIgnoreCase(name));
		
		return this.companyRepository.count(cond)>0;
	}

	public void delete(Company toDelete) {
		if(toDelete==null) {
			throw new NullPointerException();
		}
		
		this.companyRepository.delete(toDelete);
	}
	
	
	
}
