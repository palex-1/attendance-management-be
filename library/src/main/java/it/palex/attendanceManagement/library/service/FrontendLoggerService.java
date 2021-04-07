package it.palex.attendanceManagement.library.service;

import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.library.rest.dtos.FeLoggingDTO;

/**
 * @author Alessandro Pagliaro
 *
 */
@Component
public class FrontendLoggerService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FrontendLoggerService.class);

	
	public void logInfo(FeLoggingDTO logDTO, String userAgent){
		if(logDTO!=null){
			LOGGER.error("User-Agent:"+userAgent+"\n"+logDTO.toString());
		}
	}
	
	public void logError(FeLoggingDTO logDTO, String userAgent){
		if(logDTO!=null){
			LOGGER.error("User-Agent:"+userAgent+"\n"+logDTO.toString());
		}
	}
	
	public void logWarning(FeLoggingDTO logDTO, String userAgent){
		if(logDTO!=null){
			LOGGER.error("User-Agent:"+userAgent+"\n"+logDTO.toString());
		}
	}
	
	public void logDebug(FeLoggingDTO logDTO, String userAgent){
		if(logDTO!=null){
			LOGGER.error("User-Agent:"+userAgent+"\n"+logDTO.toString());
		}
	}
}
