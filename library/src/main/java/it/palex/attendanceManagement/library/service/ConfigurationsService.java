package it.palex.attendanceManagement.library.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationsService {

	@Value("${security.proxy.type:NGINX}")
	private String proxyType;
		
	public String getProxyType() {
		return this.proxyType;
	}

	@Value("${temp-files.time-to-live-in-seconds:60}")
	private int tempFileTimeToLiveSeconds;

	public int getTempFileTimeToLiveSeconds() {
		return this.tempFileTimeToLiveSeconds;
	}


}
