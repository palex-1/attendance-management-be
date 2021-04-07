package it.palex.attendanceManagement.library.utils.crypto;

/**
 * @author Alessandro Pagliaro
 *
 */
public class RandomPasswordGenerator {


	public static String generateNumericPassword(int passLength){
		if(passLength<2){
			throw new IllegalArgumentException("passLength cannot be less than 2 , passLength:"+passLength);
		}
		return SecureStringGenerator.getInstance().generateSecureNumericCasualString(passLength, false);
	}
	
	
	public static String generateAlphanumericPassword(int passLength){
		if(passLength<2){
			throw new IllegalArgumentException("passLength cannot be less than 2 , passLength:"+passLength);
		}
		return SecureStringGenerator.getInstance().generateSecureCharacterAndNumeric(passLength, true);
	}
	
	
	public static String generateAlphanumericWithSpecialCharacter(int passLength){
		if(passLength<2){
			throw new IllegalArgumentException("passLength cannot be less than 2 , passLength:"+passLength);
		}
		return SecureStringGenerator.getInstance().generateSecureCharacterNumericAndLESSpecialCharacters(passLength);
	}
	
	
}
