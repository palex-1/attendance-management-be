package it.palex.attendanceManagement.commons.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import it.palex.attendanceManagement.library.utils.crypto.HMacOneTimePasswordGenerator;
import it.palex.attendanceManagement.library.utils.crypto.SecureStringGenerator;

public class OneTimePasswordGeneratorService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OneTimePasswordGeneratorService.class);
		
	private HMacOneTimePasswordGenerator generator;
	private final int passwordLenght;
	private final String hmacSecret;
		
	public OneTimePasswordGeneratorService(int passwordLenght) {
		if(passwordLenght<6) {
			throw new IllegalArgumentException();
		}
		this.generator = new HMacOneTimePasswordGenerator();
		this.passwordLenght = passwordLenght;
		this.hmacSecret = SecureStringGenerator.getInstance().generateSecureCharacterAndNumeric(12, false);
	}
	
	
	/**
	 * 
	 * @return a numeric OTP of 8 chars
	 */
	public String generateHmacNumericOneTimePassword() {		
		try {
			
			return this.generator.generateOTP(this.hmacSecret);
			
		} catch (InvalidKeyException e) {
			LOGGER.error("", e);
			throw new RuntimeException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("", e);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public String generateAlphaNumericOneTimePassword() {
		return SecureStringGenerator.getInstance().generateSecureCharacterAndNumeric(this.passwordLenght, true);
	}
	
	public String generateAlphaNumericOneTimePassword(int length) {
		if(length < 6) {
			throw new IllegalArgumentException("one time password cannot be less than 6 char");
		}
		return SecureStringGenerator.getInstance().generateSecureCharacterAndNumeric(length, true);
	}
	
	
}
