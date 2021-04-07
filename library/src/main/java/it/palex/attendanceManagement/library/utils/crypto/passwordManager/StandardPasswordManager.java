package it.palex.attendanceManagement.library.utils.crypto.passwordManager;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import it.palex.attendanceManagement.library.utils.crypto.sym.StringEncryptionAESUtil;


public class StandardPasswordManager implements PasswordManager{
	
	private char[] masterPassword = null;
	
	public StandardPasswordManager(char[] masterPassword) {
		if(masterPassword==null) {
			throw new NullPointerException();
		}
		this.masterPassword = Arrays.copyOf(masterPassword, masterPassword.length);
	}

	
	@Override
	public String decrypt(String pswToDecrypt) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		return decryptPassword(pswToDecrypt, masterPassword);
	}

	@Override
	public String encrypt(String pswToEncrypt) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		return encryptPassword(pswToEncrypt, masterPassword);
	}
	
	public static String decryptPassword(String toDecrypt, char[] masterPassword) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		if(toDecrypt==null || masterPassword==null) {
			throw new NullPointerException();
		}
		return decryptPasswordPrivate(toDecrypt, masterPassword);
	}
	
	private static String decryptPasswordPrivate(String toDecrypt, char[] masterPassword) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		String decryptedStr  = StringEncryptionAESUtil.decrypt(toDecrypt, masterPassword);
		
		return decryptedStr;
			
	}
	
	
	public static String encryptPassword(String toEncrypt, char[] masterPassword) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		if(toEncrypt==null || masterPassword==null) {
			throw new NullPointerException();
		}
		return encryptPasswordPrivate(toEncrypt, masterPassword);
	}
	
	private static String encryptPasswordPrivate(String toEncrypt, char[] masterPassword) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		final String encryptedStr = StringEncryptionAESUtil.encrypt(toEncrypt, masterPassword);
		
		return encryptedStr;
	}
	
}
