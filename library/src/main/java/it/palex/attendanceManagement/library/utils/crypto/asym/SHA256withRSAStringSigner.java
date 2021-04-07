package it.palex.attendanceManagement.library.utils.crypto.asym;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Base64;


/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public class SHA256withRSAStringSigner {

	/**
	 * 
	 * @param data to be sign
	 * @param key to sign data
	 * @return signed <strong>data</strong>
	 * @throws SignatureException
	 * @throws InvalidKeyException
	 * @throws NullPointerException if at least one parameter is null
	 */
	public static byte[] sign(String data, PrivateKey key) throws InvalidKeyException, SignatureException{
		if(data==null || key==null){
			throw new NullPointerException();
		}
		Signature rsa = getSHA256withRSASignature();
		rsa.initSign(key);
		rsa.update(data.getBytes());
		return rsa.sign();
	}
	
	/**
	 * 
	 * @param data to be sign
	 * @param key to sign data
	 * @return signed <strong>data</strong> encoded in base64
	 * @throws SignatureException
	 * @throws InvalidKeyException
	 * @throws NullPointerException if at least one parameter is null
	 */
	public static String signAndGetBase64EncodedString(String data, PrivateKey key) throws SignatureException, InvalidKeyException{
		if(data==null || key==null){
			throw new NullPointerException();
		}
		byte[] signed = sign(data, key);
		String base64Encoded = Base64.getEncoder().encodeToString(signed);
		
		return base64Encoded;
	}
	
	
	private static Signature getSHA256withRSASignature(){
		try{
			Signature rsa = Signature.getInstance("SHA256withRSA");
			return rsa;
		}catch(NoSuchAlgorithmException e){
			throw new RuntimeException("Strange error: All jre must implement SHA256withRSA "+Arrays.toString(e.getStackTrace()));
		}
	}
	
	
}
