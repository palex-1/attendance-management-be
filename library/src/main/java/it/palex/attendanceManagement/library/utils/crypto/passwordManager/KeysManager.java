package it.palex.attendanceManagement.library.utils.crypto.passwordManager;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeysManager {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(KeysManager.class);
	
	@Autowired
	private PasswordManager passwordManager;
	
	
	@Value("${fileManager.EncryptedFSFileManagerPassword.password:@null}")
	private String encryptedFileManagerPassword;
	
	public String getEncryptedFileManagerPassword() {
		return encryptedFileManagerPassword;
	}
	
	
	@Value("${persistence.datasource.password:@null}")
	private String databaseKey;
	
	public String getDatabasePassword() {
		return databaseKey;
	}
	
	@Value("${turnstile.secret.password:@null}")
	private String turnstileKey;
	
	public String getTurnstileEncrypterPassword() {
		return turnstileKey;
	}
	
	
	private String decryptSecret(String secret) {
		try {
			if(secret==null) {
				return null;
			}
			return this.passwordManager.decrypt(secret);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | UnsupportedEncodingException e) {
			LOGGER.error("", e);
			throw new RuntimeException(e);
		}
	}




	
	
}

