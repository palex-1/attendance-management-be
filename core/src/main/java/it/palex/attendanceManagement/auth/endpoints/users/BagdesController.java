package it.palex.attendanceManagement.auth.endpoints.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import it.palex.attendanceManagement.auth.service.users.BadgeEmployeeGenerator;
import it.palex.attendanceManagement.data.dto.documents.DocumentoReadInternalResponse;
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
 * 30 giu 2020
 */
@RestController
@RequestMapping(path="badges")
public class BagdesController extends RestEndpoint {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BagdesController.class);

	@Autowired
	private BadgeEmployeeGenerator badgeEmployeeGenerator;
	
	
	@GetMapping(value = "/companyId")
	@Loggable()
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	public ResponseEntity<GenericResponse<StringDTO>> findCompanyId() {
		try {
			GenericResponse<StringDTO> res = this.badgeEmployeeGenerator.findCompanyId();
			
			return this.buildGenericResponse(res);
		}catch(Exception e) {
			LOGGER.error("error during document retrive", e);
			return ResponseEntity.status(HttpCodes.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	
	
	@GetMapping(value = "/generate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.USER_PROFILE_READ+"')")
	public ResponseEntity<StreamingResponseBody> openStreamToDownloadFile(
			@RequestParam(name = "userProfileId") Integer userProfileId) {
		DocumentoReadInternalResponse response = null;
		
		try {
			response = this.badgeEmployeeGenerator.generateBadgeForUser(userProfileId);
			
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
