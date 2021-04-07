package it.palex.attendanceManagement.core.controllers.rest.endpoints.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.FeLoggingDTO;
import it.palex.attendanceManagement.library.service.FrontendLoggerService;

/**
 * @author Alessandro Pagliaro
 *
 */
@RestController
@RequestMapping("fe-logging")
@OpenAPIDefinition(
	    info = @Info(
	        title = "Endpoint to store logs of Frontend"
	    )
	)
public class FrontendLoggerRC extends RestEndpoint{

	@Autowired
	private FrontendLoggerService feLoggerSrv;
	
	@PostMapping(path="/info")
	public void logInfo(@RequestHeader("user-agent") String userAgent, 
			   @RequestBody FeLoggingDTO logDTO){
		this.feLoggerSrv.logInfo(logDTO, userAgent);
	}
	
	@PostMapping(path="/error")
	public void logError(@RequestHeader("user-agent") String userAgent, 
			   @RequestBody FeLoggingDTO logDTO){
		this.feLoggerSrv.logInfo(logDTO, userAgent);
	}
	
	@PostMapping(path="/warn")
	public void logWarning(@RequestHeader("user-agent") String userAgent, 
			   @RequestBody FeLoggingDTO logDTO){
		this.feLoggerSrv.logWarning(logDTO, userAgent);
	}
	
	@PostMapping(path="/debug")
	public void logDebug(@RequestHeader("user-agent") String userAgent, 
			   @RequestBody FeLoggingDTO logDTO){
		this.feLoggerSrv.logDebug(logDTO, userAgent);
	}
	
}
