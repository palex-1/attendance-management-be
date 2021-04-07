package it.palex.attendanceManagement.core.controllers.rest.endpoints.documento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import it.palex.attendanceManagement.data.dto.documents.DocumentoReadInternalResponse;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.HttpCodes;

/**
 * @author Alessandro Pagliaro
 *
 */
@RestController
@RequestMapping(path="documents")
public class DocumentsDownloadRC extends RestEndpoint {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DocumentsDownloadRC.class);

	@Autowired
	private DocumentService documentService;
	

	@GetMapping(value = "/download-file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<StreamingResponseBody> openStreamToDownloadFile(
			@RequestParam(name="downloadToken", required = true) String downloadToken) {
		DocumentoReadInternalResponse response = null;
		
		try {
			response = this.documentService.openStreamOnFile(downloadToken);
			
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
