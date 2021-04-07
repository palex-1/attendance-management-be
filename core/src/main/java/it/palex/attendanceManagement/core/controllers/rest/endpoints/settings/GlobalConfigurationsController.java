package it.palex.attendanceManagement.core.controllers.rest.endpoints.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import it.palex.attendanceManagement.core.service.settings.GlobalConfigurationsWebService;
import it.palex.attendanceManagement.data.dto.documents.DocumentoReadInternalResponse;
import it.palex.attendanceManagement.data.dto.settings.GlobalConfigurationsDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.HttpCodes;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 3 lug 2020
 */
@RestController
@RequestMapping(path="configs")
public class GlobalConfigurationsController extends RestEndpoint {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GlobalConfigurationsController.class);
	
	@Autowired
	private GlobalConfigurationsWebService globalConfigurationsWebService;
	
	@PostMapping(path = "create")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_GLOBAL_CONFIGURATIONS+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<GlobalConfigurationsDTO>> create(
			@RequestBody GlobalConfigurationsDTO toCreate) {
		
		GenericResponse<GlobalConfigurationsDTO> res = this.globalConfigurationsWebService.create(toCreate);
		
		return this.buildGenericResponse(res);
	}
	
	@PostMapping(path = "updateValue")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_GLOBAL_CONFIGURATIONS+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<GlobalConfigurationsDTO>> updateValue(
			@RequestBody GlobalConfigurationsDTO toUpdate) {
		
		GenericResponse<GlobalConfigurationsDTO> res = this.globalConfigurationsWebService.updateValue(toUpdate);
		
		return this.buildGenericResponse(res);
	}
	
	@DeleteMapping(path = "deleteArea")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_GLOBAL_CONFIGURATIONS+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> deleteArea(
			@RequestParam(value="area", required= true) String area) {
		
		GenericResponse<StringDTO> res = this.globalConfigurationsWebService.deleteArea(area);
		
		return this.buildGenericResponse(res);
	}
	
	@DeleteMapping(path = "delete")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_GLOBAL_CONFIGURATIONS+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> delete(
			@RequestParam(value="id", required= true) Integer id) {
		
		GenericResponse<StringDTO> res = this.globalConfigurationsWebService.deleteConfig(id);
		
		return this.buildGenericResponse(res);
	}
	
	@GetMapping(path = "findAllAreas")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_GLOBAL_CONFIGURATIONS+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<StringDTO>>> findAllAreas(
			@RequestParam(value="area", required= false) String area,
			@PageableDefault(page = 0, size = 10, sort={"settingArea"}, direction=Direction.ASC) Pageable pageable) {
		
		GenericResponse<Page<StringDTO>> res = this.globalConfigurationsWebService
				.findAllConfigurationAreas(area, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path = "findAllSettingsOfArea")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_GLOBAL_CONFIGURATIONS+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<GlobalConfigurationsDTO>>> findAllSettingsOfArea(
			@RequestParam(value="area", required= true) String area,
			@RequestParam(value="key", required= false) String key,
			@PageableDefault(page = 0, size = 10, sort={"id"}, direction=Direction.DESC) Pageable pageable) {
		
		GenericResponse<Page<GlobalConfigurationsDTO>> res = this.globalConfigurationsWebService
				.findAllConfigurationsByArea(area, key, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@PostMapping("/imageLogo")
    @Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.MANAGE_GLOBAL_CONFIGURATIONS+"')")
    public ResponseEntity<GenericResponse<StringDTO>> uploadLogoImage(
    		@RequestPart("image") MultipartFile file) throws Exception {
        GenericResponse<StringDTO> response = this.globalConfigurationsWebService.uploadLogoImage(file);
        
        return this.buildGenericResponse(response);
    }
	
	
	@GetMapping(value = "/downloadImageLogo", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<StreamingResponseBody> openStreamToDownloadImageLogo() {
		DocumentoReadInternalResponse response = null;
		
		try {
			response = this.globalConfigurationsWebService
					.openStreamOnLogoImage();
			
			if(response==null) {
				return ResponseEntity.notFound().build();
			}
			
			return ResponseEntity
	                .ok()
	                .header("content-disposition","attachment; filename = "+response.getFileName())
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(response.getResponseBody());
			
		}catch(Exception e) {
			LOGGER.error("error during document retrive", e);
			return ResponseEntity.status(HttpCodes.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
