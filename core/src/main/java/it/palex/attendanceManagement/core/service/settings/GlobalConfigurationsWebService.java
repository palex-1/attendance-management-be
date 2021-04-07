package it.palex.attendanceManagement.core.service.settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.palex.attendanceManagement.data.dto.documents.DocumentoReadInternalResponse;
import it.palex.attendanceManagement.data.dto.settings.GlobalConfigurationsDTO;
import it.palex.attendanceManagement.data.dto.transformers.core.GlobalConfigurationsTransformer;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.entities.enumTypes.StandardDocumentDescription;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.StringUtility;

@Service
public class GlobalConfigurationsWebService implements GenericService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GlobalConfigurationsWebService.class);
			
	@Autowired
	private GlobalConfigurationsService globalConfigurationsService;
	
	@Autowired
	private DocumentService documentService;
	
	
	public GenericResponse<Page<StringDTO>> findAllConfigurationAreas(String area, Pageable pageable){
		if(pageable==null) {
			throw new NullPointerException();
		}
		Pair<List<String>, Long> pair = this.globalConfigurationsService.findAllAreasAndCount(area, false, pageable);
		List<StringDTO> res = new ArrayList<StringDTO>(pair.getKey().size());
		
		for(String value: pair.getKey()) {
			res.add(new StringDTO(value));
		}
		
		return this.buildPageableOkResponse(res, pair.getValue(), pageable);
	}

	
	public GenericResponse<Page<GlobalConfigurationsDTO>> findAllConfigurationsByArea(String area, String key, Pageable pageable){
		if(area==null || pageable==null) {
			return this.buildBadDataResponse();
		}
		
		Pair<List<GlobalConfigurations>, Long> pair = this.globalConfigurationsService.findAllByAreaAndCount(area, key, false, pageable);
		
		List<GlobalConfigurationsDTO> res = new ArrayList<GlobalConfigurationsDTO>(pair.getKey().size());
		
		for(GlobalConfigurations config: pair.getKey()) {
			res.add(GlobalConfigurationsTransformer.mapToDTO(config));
		}
		
		return this.buildPageableOkResponse(res, pair.getValue(), pageable);
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> deleteConfig(Integer id) {
		if(id==null) {
			return this.buildBadDataResponse();
		}
		GlobalConfigurations oldConfig = this.globalConfigurationsService.findById(id);
		
		if(oldConfig==null) {
			return this.buildNotFoundResponse();
		}
		
		this.globalConfigurationsService.delete(oldConfig);
		
		return this.buildStringOkResponse("Successfully deleted");
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> deleteArea(String area) {
		if(area==null) {
			return this.buildBadDataResponse();
		}
		
		this.globalConfigurationsService.deleteArea(area);
		
		return this.buildStringOkResponse("Successfully deleted");
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<GlobalConfigurationsDTO> updateValue(GlobalConfigurationsDTO toUpdate) {
		if(toUpdate==null || StringUtility.isEmptyOrWhitespace(toUpdate.getSettingArea()) || StringUtility.isEmptyOrWhitespace(toUpdate.getSettingKey())
				|| StringUtility.isEmptyOrWhitespace(toUpdate.getSettingValue())) {
			return this.buildBadDataResponse();
		}
		
		String value = StringUtils.trim(toUpdate.getSettingValue());
		
		GlobalConfigurations oldConfig = this.globalConfigurationsService.findByAreaAndKey(toUpdate.getSettingArea(), toUpdate.getSettingKey());
		
		oldConfig.setSettingValue(value);
		
		if(!oldConfig.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		oldConfig = this.globalConfigurationsService.saveOrUpdate(oldConfig);
		
		return this.buildOkResponse(GlobalConfigurationsTransformer.mapToDTO(oldConfig));
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<GlobalConfigurationsDTO> create(GlobalConfigurationsDTO toCreate) {
		if(toCreate==null || StringUtility.isEmptyOrWhitespace(toCreate.getSettingArea()) || StringUtility.isEmptyOrWhitespace(toCreate.getSettingKey())
				|| StringUtility.isEmptyOrWhitespace(toCreate.getSettingValue())) {
			return this.buildBadDataResponse();
		}
		
		String area = StringUtils.trim(toCreate.getSettingArea());
		String key = StringUtils.trim(toCreate.getSettingKey());
		String value = StringUtils.trim(toCreate.getSettingValue());
		
		GlobalConfigurations oldConfig = this.globalConfigurationsService.findByAreaAndKey(area, key);
		
		if(oldConfig!=null) {
			return this.buildConflictEntity();
		}
		
		GlobalConfigurations config = new GlobalConfigurations();
		config.setSettingArea(area);
		config.setSettingKey(key);
		config.setSettingValue(value);
		config.setEditable(true);
		config.setVisible(true);
		
		if(!config.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		config = this.globalConfigurationsService.saveOrUpdate(config);
		
		return this.buildOkResponse(GlobalConfigurationsTransformer.mapToDTO(config));
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> uploadLogoImage(MultipartFile file) throws IOException, Exception {
		if(file==null) {
			return this.buildBadDataResponse();
		}
		
		Document document = this.documentService.saveImageWithDefaultFM(file.getOriginalFilename(),
				file.getInputStream(), "SYSTEM", StandardDocumentDescription.LOGO_IMAGE.name());
		
		GlobalConfigurations companyLogoDocumentId = this.globalConfigurationsService.findByAreaAndKey(
				GlobalConfigurationSettingsTuple.BADGE.AREA_NAME, 
				GlobalConfigurationSettingsTuple.BADGE.COMPANY_LOGO_DOCUMENT_ID);
		
		
		if(companyLogoDocumentId==null) {
			companyLogoDocumentId = new GlobalConfigurations();
			companyLogoDocumentId.setEditable(true);
			companyLogoDocumentId.setVisible(false);
			companyLogoDocumentId.setSettingKey(GlobalConfigurationSettingsTuple.BADGE.COMPANY_LOGO_DOCUMENT_ID);
			companyLogoDocumentId.setSettingArea(GlobalConfigurationSettingsTuple.BADGE.AREA_NAME);
			
		}else {
			//try to delete the old document
			try {
				Long documentId = Long.parseLong(companyLogoDocumentId.getSettingValue());
				Document doc = this.documentService.findDocumentById(documentId);
				this.documentService.deleteDocumentAndFile(doc);
			}catch(Exception e) {
				LOGGER.error("", e);
			}
		}
		
		companyLogoDocumentId.setSettingValue(document.getId()+"");
		
		
		companyLogoDocumentId = this.globalConfigurationsService.saveOrUpdate(companyLogoDocumentId);
				
		return this.buildStringOkResponse("Logo Image Updated");
	}


	public DocumentoReadInternalResponse openStreamOnLogoImage() throws Exception {
		GlobalConfigurations companyLogoDocumentId = this.globalConfigurationsService.findByAreaAndKey(
				GlobalConfigurationSettingsTuple.BADGE.AREA_NAME, 
				GlobalConfigurationSettingsTuple.BADGE.COMPANY_LOGO_DOCUMENT_ID);
		
		
		if(companyLogoDocumentId==null) {
			return null;
		}
		
		Long documentId = Long.parseLong(companyLogoDocumentId.getSettingValue());
		Document doc = this.documentService.findDocumentById(documentId);
				
		return this.documentService.openStreamOnFile(doc);
	}
	
	
}
