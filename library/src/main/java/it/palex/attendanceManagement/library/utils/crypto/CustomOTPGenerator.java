package it.palex.attendanceManagement.library.utils.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 */
public class CustomOTPGenerator {
	
	public static final int MIN_OTP_LENGTH = 6;
	
	public static char[] buildNumericOTP(int length) { 
		if(length<MIN_OTP_LENGTH) {
			throw new IllegalArgumentException("OTP cannot be less than "+MIN_OTP_LENGTH);
		}
		
        String numbers = "0123456789"; 
  
        SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		} 
  
        final char[] otp = new char[length]; 
  
        for (int i = 0; i < length; i++) {  
            otp[i] = numbers.charAt(random.nextInt(numbers.length())); 
        } 
        return otp; 
    } 
	
	public static char[] buildAlphaNumericOTP(int length) {
		if(length<MIN_OTP_LENGTH) {
			throw new IllegalArgumentException("OTP cannot be less than "+MIN_OTP_LENGTH);
		}
		final String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
		final String Small_chars = "abcdefghijklmnopqrstuvwxyz"; 
        final String numbers = "0123456789";
        
        final String values = Capital_chars + Small_chars + numbers; 
        
        SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		}
        
        char[] password = new char[length];
        
        for (int i = 0; i < length; i++) 
        { 
            password[i] = values.charAt(random.nextInt(values.length())); 
  
        } 
        return password;
	}
	
}
