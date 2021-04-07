package it.palex.attendanceManagement.library.utils.crypto.hmac;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSigner {

	public static final int HMAC_MD5_LENGHT = 32;
	public static final int HMAC_SHA1_LENGHT = 40;
	public static final int HMAC_SHA256_LENGHT = 64;
	public static final int HMAC_SHA512_LENGHT = 128;

	private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}

		String formatted = formatter.toString();
		formatter.close();
		
		return formatted;
	}

	public static String calculateHmac(String alg, String data, String key) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		if(alg==null || data==null || key==null) {
			throw new NullPointerException();
		}
		switch(alg) {
			case "MD5":{
				return calculateHmacMd5(data, key);
			}
			case "SHA1":{
				return calculateHmacSha1(data, key);
			}
			case "SHA256":{
				return calculateHmacSha256(data, key);
			}
			case "SHA512":{
				return calculateHmacSha512(data, key);
			}
			default : {
				return calculateHmacSha1(data, key);
			}
		}
		
	}
	
	public static String calculateHmacMd5(String data, String key)
		throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacMD5");
		Mac mac = Mac.getInstance("HmacMD5");
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}
	
	/**
	 * RFC2104HMAC
	 * @param data
	 * @param key
	 * @return
	 * @throws SignatureException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String calculateHmacSha1(String data, String key)
		throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}
	
	public static String calculateHmacSha256(String data, String key)
			throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}
	
	public static String calculateHmacSha512(String data, String key)
			throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
		Mac mac = Mac.getInstance("HmacSHA512");
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}

	public static boolean verifyHmacSignature(String signature, String data, String key) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		if(signature==null || data==null || key==null) {
			throw new NullPointerException();
		}
		int length = signature.length();
		switch(length) {
			case HMAC_SHA1_LENGHT: {
				return calculateHmacSha1(data, key).equals(signature);
			}
			case HMAC_SHA256_LENGHT: {
				return calculateHmacSha256(data, key).equals(signature);
			}
			case HMAC_SHA512_LENGHT: {
				return calculateHmacSha512(data, key).equals(signature);
			}
			case HMAC_MD5_LENGHT: {
				return calculateHmacMd5(data, key).equals(signature);
			}
			default :{
				return false;
			}
		}
	}
	
	
}
