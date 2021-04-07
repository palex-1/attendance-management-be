package it.palex.attendanceManagement.library.utils.crypto;

import org.apache.commons.lang3.RandomStringUtils;

import it.palex.attendanceManagement.library.utils.crypto.sym.EncryptionUtil;

/**
 * @author Alessandro Pagliaro
 *
 */
public class TokenGenerator {

	/**
	 * 
	 * @return SecureToken of 128 Characters
	 */
	public static String generateSecureTokenOf128Characters() {
		String casualString = generateSecureCasualString(128);
		return EncryptionUtil.sha512Crypt(casualString);
	}
	
	public static String generateTokenOf128Characters() {
		int length = 128;
		String casualString = genererateLessSecureString(length);
	    return EncryptionUtil.sha512Crypt(casualString);
	}

	/**
	 * 
	 * @return SecureToken of 96 Characters
	 */
	public static String generateSecureTokenOf96Characters() {
		String casualString = generateSecureCasualString(96);
		return EncryptionUtil.sha384Crypt(casualString);
	}
	
	public static String generateTokenOf96Characters() {
		int length = 96;
		String casualString = genererateLessSecureString(length);
	    return EncryptionUtil.sha384Crypt(casualString);
	}

	/**
	 * 
	 * @return SecureToken of 64 Characters
	 */
	public static String generateSecureTokenOf64Characters() {
		String casualString = generateSecureCasualString(64);
		return EncryptionUtil.sha256Crypt(casualString);
	}
	
	public static String generateTokenOf64Characters() {
		int length = 64;
		String casualString = genererateLessSecureString(length);
	    return EncryptionUtil.sha256Crypt(casualString);
	}

	/**
	 * 
	 * @return SecureToken of 40 Characters
	 */
	public static String generateSecureTokenOf40Characters() {
		String casualString = generateSecureCasualString(40);
		return EncryptionUtil.sha1Crypt(casualString);
	}

	public static String generateTokenOf40Characters() {
		int length = 40;
		String casualString = genererateLessSecureString(length);
	    return EncryptionUtil.sha1Crypt(casualString);
	}
	
	/**
	 * 
	 * @return SecureToken of 32 Characters
	 */
	public static String generateSecureTokenOf32Characters() {
		String casualString = generateSecureCasualString(32);
		return EncryptionUtil.md5Crypt(casualString);
	}

	public static String generateTokenOf32Characters() {
		int length = 32;
		String casualString = genererateLessSecureString(length);
	    return EncryptionUtil.md5Crypt(casualString);
	}
	
	private static String generateSecureCasualString(int targetStringLength) {
		return SecureStringGenerator.getInstance()
				.generateSecureCharacterNumericAndSpecialCharacters(targetStringLength);
	}
	
	
	private static String genererateLessSecureString(int length) {
		boolean useLetters = true;
	    boolean useNumbers = false;
	    
	    return RandomStringUtils.random(length, useLetters, useNumbers);
	}

}
