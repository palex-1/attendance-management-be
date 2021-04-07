package it.palex.attendanceManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.ApplicationVersionService;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@RequestMapping(path = "version")
@RestController
public class VersionInfo extends RestEndpoint {

	@Autowired
	private ApplicationVersionService versionSrv;
	
	@GetMapping()
	@Loggable(logExecutionTime = true, logParameters = true, logResponseParameter = true)
	public ResponseEntity<GenericResponse<StringDTO>> getApplicationVersion(){
		try{
			
			GenericResponse<StringDTO> appVersion = this.versionSrv.getApplicationVersion();
			
			return this.buildSuccessResponse(appVersion, appVersion.getData().getValue());
		}catch(Exception e) {
			return this.buildInternalErrorResponse(e, this.versionSrv.getVersion());
		}
	}
	
	
	
}
