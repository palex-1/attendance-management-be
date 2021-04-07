package it.palex.attendanceManagement.library.utils.crypto.asym;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Base64;


/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public class SHA256withRSAStringVerifier {

	/**
	 * 
	 * @param originalData
	 * @param signature
	 * @param key
	 * @return
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws NullPointerException if at least one parameter is null
	 */
	public static boolean verifySignature(byte[] originalData, byte[] signature, PublicKey key) throws InvalidKeyException, SignatureException {
		if(originalData==null || signature==null || key==null){
			throw new NullPointerException();
		}
		Signature sig = getSHA256withRSASignature();
		sig.initVerify(key);
		sig.update(originalData);

		return sig.verify(signature);
	}
	
	/**
	 * 
	 * @param originalData
	 * @param base64EncodedSignature
	 * @param key
	 * @return true if the signature is valid
	 * @throws SignatureException
	 * @throws InvalidKeyException
	 * @throws NullPointerException if at least one parameter is null
	 */
	public static boolean verifySignatureBase64Encoded(byte[] originalData,  String base64EncodedSignature, PublicKey key) throws SignatureException, InvalidKeyException{
		if(originalData==null || base64EncodedSignature==null || key==null){
			throw new NullPointerException();
		}
		
		byte[] base64Decoded = Base64.getDecoder().decode(base64EncodedSignature);
		
		return verifySignature(originalData, base64Decoded, key);
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
