package it.palex.attendanceManagement.data.service.sede;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import it.palex.attendanceManagement.data.dto.sede.SedeLavorativaInDTO;
import it.palex.attendanceManagement.data.dto.sede.SedeLavorativaOutDTO;
import it.palex.attendanceManagement.data.dto.sede.SedeLavorativaUpdateDTO;
import it.palex.attendanceManagement.data.entities.Office;
import it.palex.attendanceManagement.data.entities.QOffice;
import it.palex.attendanceManagement.data.entities.enumTypes.CompanyBranchType;
import it.palex.attendanceManagement.data.repository.core.OfficeRepository;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;

/**
 * @author Alessandro Pagliaro
 *
 */
@Service
public class OfficeService implements GenericService {

	private final QOffice QSL = QOffice.office;

	
	@Autowired
	private OfficeRepository officeRepo;
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<SedeLavorativaOutDTO> create(SedeLavorativaInDTO sede){
		if(sede==null || sede.getNomeSede()==null) {
			return this.buildBadDataResponse();
		}
		Office entity = mapToEntity(sede);
		if(entity==null || !entity.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		if(this.getByOfficeName(sede.getNomeSede())!=null) {
			return this.buildConflictEntity();
		}
		entity = this.officeRepo.save(entity);
		
		SedeLavorativaOutDTO out = mapToDTO(entity);
		
		return this.buildOkResponse(out);
	}
	
	
	public Office getByOfficeName(String nomeSede){
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QSL.officeName.containsIgnoreCase(nomeSede));
		Iterator<Office> it = this.officeRepo.findAll(condition).iterator();
		if(it.hasNext()) {
			return it.next();
		}
		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<SedeLavorativaOutDTO> update(SedeLavorativaUpdateDTO sede){
		if(sede==null || sede.getId()==null) {
			return this.buildBadDataResponse();
		}
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QSL.id.eq(sede.getId()));
		Iterator<Office> it = this.officeRepo.findAll(condition).iterator();
		if(!it.hasNext()) {
			return this.buildNotFoundResponse();
		}
		if(!isValidSedeLavorativaUpdate(sede)) {
			return this.buildBadDataResponse();
		}
		Office entity = it.next();
		//se il nome della sede e' stato aggiornato ed e' utilizzato
		if(!StringUtils.equalsIgnoreCase(entity.getOfficeName(), sede.getNomeSede()) && 
				this.getByOfficeName(sede.getNomeSede())!=null) {
			return this.buildConflictEntity();
		}
		
		updateSedeLavorativa(entity, sede);
		
		entity = this.officeRepo.save(entity);
		
		SedeLavorativaOutDTO out = mapToDTO(entity);
		
		return this.buildOkResponse(out);
	}
	
	public GenericResponse<Page<SedeLavorativaOutDTO>> findAll(Pageable pageable, Predicate predicate, 
			String nomeSede, String via, String companyBranchType, String citta, 
			String provincia, String nazione, String cap) {
		if(pageable==null) {
			return this.buildBadDataResponse();
		}
		BooleanBuilder condition = this.buildContainsFilter(predicate, nomeSede,
				via, companyBranchType, citta, provincia, nazione, cap);
		
		
		Iterator<Office> it = this.officeRepo.findAll(condition, pageable).iterator();
		List<SedeLavorativaOutDTO> list = new ArrayList<>();
		while(it.hasNext()) {
			Office park = it.next();
			list.add(mapToDTO(park));
		}
		
		long totalCount = this.officeRepo.count(condition);
		
		return this.buildPageableOkResponse(list, totalCount, pageable);
		
	}
	
	private BooleanBuilder buildContainsFilter(Predicate predicate, 
			String nomeSede, String via, String companyBranchType, String citta, 
			String provincia, String nazione, String cap) {
		BooleanBuilder condition = new BooleanBuilder(predicate);
		if(nomeSede!=null) {
			condition.and(QSL.officeName.containsIgnoreCase(nomeSede));
		}
		if(via!=null) {
			condition.and(QSL.street.containsIgnoreCase(via));
		}
		if(citta!=null) {
			condition.and(QSL.city.containsIgnoreCase(citta));
		}
		if(provincia!=null) {
			condition.and(QSL.province.containsIgnoreCase(provincia));
		}
		if(nazione!=null) {
			condition.and(QSL.nation.containsIgnoreCase(nazione));
		}
		if(cap!=null) {
			condition.and(QSL.zipCode.containsIgnoreCase(cap));
		}
		if(companyBranchType!=null) {
			condition.and(QSL.companyBranchType.containsIgnoreCase(companyBranchType));
		}
		
		return condition;
	}
	
	
	private static boolean isValidSedeLavorativaUpdate(SedeLavorativaUpdateDTO update) {
		Office park = new Office();
		updateSedeLavorativa(park, update);
		
		boolean isValid = park.canBeInsertedInDatabase();
		park = null;
		return isValid;
	}
	
	public GenericResponse<List<StringDTO>> getAllTipoSedi() {
		List<CompanyBranchType> tipi = CompanyBranchType.getAll();
		List<StringDTO> res = new LinkedList<>();
		for(CompanyBranchType tipo: tipi) {
			res.add(new StringDTO(tipo.name()));
		}
		
		return this.buildOkResponse(res);
	}
	
	private static void updateSedeLavorativa(Office sede, SedeLavorativaUpdateDTO update) {
		sede.setZipCode(update.getCap());
		sede.setCity(update.getCitta());
		sede.setNation(update.getNazione());
		sede.setOfficeName(update.getNomeSede());
		sede.setProvince(update.getProvincia());
		sede.setCompanyBranchType(null); //if is invalid set to null
		
		if(update.getTipoSede()!=null && CompanyBranchType.isValid(update.getTipoSede())) {
			sede.setCompanyBranchType(update.getTipoSede());
		}
		sede.setStreet(update.getVia());
	}
	
	private static SedeLavorativaOutDTO mapToDTO(Office sede) {
		if(sede==null) {
			return null;
		}
		SedeLavorativaOutDTO out = new SedeLavorativaOutDTO();
		out.setCap(sede.getZipCode());
		out.setCitta(sede.getCity());
		out.setId(sede.getId());
		out.setNazione(sede.getNation());
		out.setNomeSede(sede.getOfficeName());
		out.setProvincia(sede.getProvince());
		out.setTipoSede(sede.getCompanyBranchType()+"");
		out.setVia(sede.getStreet());
		
		return out;
	}
	
	private static Office mapToEntity(SedeLavorativaInDTO in) {
		if(in==null) {
			return null;
		}
		Office sede = new Office();
		sede.setZipCode(in.getCap());
		sede.setCity(in.getCitta());
		sede.setNation(in.getNazione());
		sede.setOfficeName(in.getNomeSede());
		sede.setProvince(in.getProvincia());
		
		sede.setCompanyBranchType(null); //if is invalid set to null
		if(in.getTipoSede()!=null && CompanyBranchType.isValid(in.getTipoSede())) {
			sede.setCompanyBranchType(in.getTipoSede());
		}
		
		sede.setStreet(in.getVia());
		
		return sede;
	}


	public GenericResponse<List<StringDTO>> findAllEmploymentOffice() {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QSL.companyBranchType.eq(CompanyBranchType.COMPANY_OFFICE.name()));
		
		Sort sort = Sort.by(Direction.ASC, "companyBranchType");
		
		Iterator<Office> it = this.officeRepo.findAll(cond, sort).iterator();
		List<StringDTO> res = new LinkedList<>();
		
		while(it.hasNext()) {
			Office office = it.next();
			StringDTO dto = new StringDTO();
			dto.setValue(office.getOfficeName());
			
			res.add(dto);
		}
		
		return this.buildOkResponse(res);
	}


	
	
}
