package it.palex.attendanceManagement.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;

@Component
public class ApplicationVersionService implements GenericService {

	@Autowired
	private Environment env;
	
	public GenericResponse<StringDTO> getApplicationVersion(){		
		return this.buildOkResponse(new StringDTO(this.getVersion()+" - "+getApplicationName()));
	}
	
	/**
	 * 
	 * @return the string 'null' if the version is not setted otherwise the version
	 */
	public String getVersion() {
		return ""+env.getProperty("application.formatted-version");
	}
	
	public String getApplicationName() {
		String appName = env.getProperty("spring.application.name");
		
		String eurekaInstance = getEurekaInstanceId();
		
		if(eurekaInstance==null) {
			return appName+"";
		}
		
		return appName+". I:"+eurekaInstance;
	}
	
	
	private String getEurekaInstanceId() {
		return env.getProperty("eureka.instance.instanceId");
	}

	
}
