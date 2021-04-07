package it.palex.attendanceManagement.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.palex.attendanceManagement.library.utils.crypto.passwordManager.PasswordManager;
import it.palex.attendanceManagement.library.utils.crypto.passwordManager.StandardPasswordManager;

@Configuration
public class PasswordManagerConfig {

	@Bean 
	public PasswordManager passwordManager() {
		final char[] inCodePassword = {'d','!','Y','$','6','7','à',')','?','g','t','7','6','#','v','d','s'};

		return new StandardPasswordManager(inCodePassword);
	}
	
}
