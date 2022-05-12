package it.palex.attendanceManagement.core;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import it.palex.attendanceManagement.library.utils.crypto.SecureStringGenerator;
import it.palex.attendanceManagement.library.utils.crypto.passwordManager.StandardPasswordManager;

/**
 * Unit test for simple App.
 */
public class AppTest {

	public static void main(String[] args) throws Exception {

		
		String psw = SecureStringGenerator.getInstance().generateSecureCharacterAndNumeric(24, true);
		final char[] inCodePassword = { 'd', '!', 'Y', '$', '6', '7', 'à', ')', '?', 'g', 't', '7', '6', '#', 'v', 'd', 's' };


		String encryptedPsw = StandardPasswordManager.encryptPassword(psw, inCodePassword);

		System.out.println("Encrypted:"+encryptedPsw);
		System.out.println("Decrypted:"+StandardPasswordManager.decryptPassword(encryptedPsw, inCodePassword));
	}
	
	
	
	
}
