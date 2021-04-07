package it.palex.attendanceManagement.core.service.company;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.dto.core.CompanyDTO;
import it.palex.attendanceManagement.data.dto.transformers.CompanyTransformer;
import it.palex.attendanceManagement.data.entities.Company;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.core.CompanyService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.HttpCodes;

@Component
public class CompanyCoreService implements GenericService {

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;

	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST})
	public GenericResponse<CompanyDTO> create(CompanyDTO company) {
		if(company==null || company.getName()==null || company.getDescription()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		String name = StringUtils.trim(company.getName());
		
		if(this.companyService.checkCompanyNameExistance(name)) {
			return this.buildConflictEntity("Another company has the same name");
		}
		
		Company toAdd = new Company();
		toAdd.setDescription(StringUtils.trim(company.getDescription()));
		toAdd.setName(name);
		toAdd.setIsRoot(Company.IS_ROOT_DEFAULT);
		
		if(!toAdd.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		toAdd = this.companyService.saveOrUpdate(toAdd);
		
		return this.buildOkResponse(CompanyTransformer.mapToDTO(toAdd));
		
	}

	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST})
	public GenericResponse<CompanyDTO> update(CompanyDTO company) {
		if(company==null || company.getName()==null || company.getId()==null || company.getDescription()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		Company toUpdate = this.companyService.findById(company.getId());
		
		if(toUpdate==null) {
			return this.buildNotFoundResponse("Company not found");
		}
		
		String name = StringUtils.trim(company.getName());
		
		if(!StringUtils.equalsIgnoreCase(toUpdate.getName(), name)) {
			if(this.companyService.checkCompanyNameExistance(name)) {
				return this.buildConflictEntity("Another company has the same name");
			}
		}
		
		toUpdate.setDescription(StringUtils.trim(company.getDescription()));
		toUpdate.setName(name);
		
		if(!toUpdate.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		toUpdate = this.companyService.saveOrUpdate(toUpdate);
		
		return this.buildOkResponse(CompanyTransformer.mapToDTO(toUpdate));
	}

	public GenericResponse<Page<CompanyDTO>> findAll(String name, String description, Pageable pageable) {
		if( pageable==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		Pair<List<Company>, Long> pair = this.companyService.findAllAndCount(name, description, pageable);
		
		List<CompanyDTO> list = CompanyTransformer.mapToDTO(pair.getKey());		
		
		return this.buildPageableOkResponse(list, pair.getValue(), pageable);
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> delete(Integer companyId) {
		if(companyId==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		Company toDelete = this.companyService.findById(companyId);
		
		if(toDelete==null) {
			return this.buildNotFoundResponse("Company not found");
		}
		
		if(BooleanUtils.isTrue(toDelete.getIsRoot())) {
			return this.buildUnprocessableEntity("Cannot delete the root company");
		}
		
		this.companyService.delete(toDelete);
		
		return this.buildStringOkResponse("Successfully deleted");
	}
	
	
	
	
	
	
}

