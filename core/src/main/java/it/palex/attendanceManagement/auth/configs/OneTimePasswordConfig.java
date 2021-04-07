package it.palex.attendanceManagement.auth.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.palex.attendanceManagement.commons.security.OneTimePasswordGeneratorService;

@Configuration
public class OneTimePasswordConfig {
	
	@Value("${security.hmac.otp.lenght}")
    private int otpLenght;
	
	
	@Bean
    public OneTimePasswordGeneratorService oneTimePasswordGeneratorService() {
    	return new OneTimePasswordGeneratorService(this.otpLenght);
    }
	
}
