package it.palex.attendanceManagement.library.utils.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.library.utils.crypto.hmac.HmacSigner;
import it.palex.attendanceManagement.library.utils.crypto.passwordManager.PasswordManager;

@Component()
public class StandardHMacSigner {

	@Value("${StandardHMacSigner.key}")
	private String key;
	
	@Value("${StandardHMacSigner.algorithm:SHA1}")
	private String algorithm;
	
	@Autowired
	private PasswordManager passwordManager;
	
	private char[] password;
	
	@PostConstruct
	private void initSigner() {
		buildPassword();
	}
	
	public String calculateSignature(String data) {
		try {
			return HmacSigner.calculateHmac(this.algorithm, data, new String(password));
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean verifySignature(String signature, String data) {
		try {
			return HmacSigner.verifyHmacSignature(signature, data, new String(password));
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	 
	private void buildPassword() {
		try {
			
			String password = this.passwordManager.decrypt(this.key);
			
			this.password = password.toCharArray();
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException
				| IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
