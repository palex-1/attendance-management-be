package it.palex.attendanceManagement.library.utils.crypto.sym;

import java.io.UnsupportedEncodingException; 
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *
 * @author Alessandro Pagliaro
 *
 */
public class EncryptionUtil {
	
	/*public static void main(String...arsg){
		System.out.println(sha256Crypt("mimmo").length());
	}*/
	
	
	/**
	 * The method return the hash of the stringToHash with SHA-384 algorithm
	 * @param stringToHash
	 * @return
	 */
	public static String sha512Crypt(String stringToHash) {
		return crypt(stringToHash, "SHA-512");
	}
	
	/**
	 * The method return the hash of the stringToHash with SHA-384 algorithm
	 * @param stringToHash
	 * @return
	 */
	public static String sha384Crypt(String stringToHash) {
		return crypt(stringToHash, "SHA-384");
	}
	
	/**
	 * The method return the hash of the stringToHash with SHA-256 algorithm
	 * @param stringToHash
	 * @return
	 */
	public static String sha256Crypt(String stringToHash) {
		return crypt(stringToHash, "SHA-256");
	}
	/**
	 * The method return the hash of the stringToHash with SHA-1 algorithm
	 * @param stringToHash
	 * @return
	 */
	public static String sha1Crypt(String stringToHash) {
		return crypt(stringToHash, "SHA-1");
	}
	
	/**
	 * The method return the hash of the stringToHash with SHA-1 algorithm
	 * @param stringToHash
	 * @return
	 */
	public static String md5Crypt(String stringToHash) {
		return crypt(stringToHash, "MD5");
	}
	
	/**
	 * Il metodo cifra la "stringToHash" con l'algoritmo di cifratura "digestAlg"
	 * @param stringToHash
	 * @param digestAlg
	 * @return
	 */
	private static String crypt(String stringToHash, String digestAlg){
		String cryptedString= null;
			
			MessageDigest md=null;
			try {
				md = MessageDigest.getInstance(digestAlg);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("This JVM does not support Algorithm:"+digestAlg);
			}
			md.update(stringToHash.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			cryptedString = sb.toString();
		
		return cryptedString;
	}
	
	
	/**
	 * The method the string in base64 to an array[]
	 * @param toDecode
	 * @return
	 */
	public static byte[] decode(String toDecode) {
		if(toDecode==null){
			throw new NullPointerException();
		}
        return Base64.getDecoder().decode(toDecode);
	}
	
	
	/**
	 * The method transform the decodedBytes in a string rapresentation with UTF-8 encoding
	 * @param decodedBytes
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(byte[] decodedBytes) throws UnsupportedEncodingException{
		if(decodedBytes==null){
			throw new NullPointerException();
		}
		return new String(decodedBytes, "UTF-8");
	}
	
	
    /**
     * The method transform a string with a base64 encoding to UTF-8
     * @param toDecode
     * @return
     * @throws UnsupportedEncodingException
     */
	public static String decodeString(String toDecode) throws UnsupportedEncodingException{
		byte[] decodedBytes=decode(toDecode);
		String decodedString=decode(decodedBytes);
		return decodedString;
	}
	
	
	/**
	 * The method encode the string toEncode in base64
	 * @param toEncode
	 * @return
	 */
	public static String encodeToBase64(String toEncode){
		byte[] toEcodedBytes=toEncode.getBytes();
		String encodedString=Base64.getEncoder().encodeToString(toEcodedBytes);
		return encodedString;
	}
	
	/**
	 * The method calculate the hash with MD5 of the string "data". <br>
	 * And then encode the hash in base64
	 * @param data
	 * @return
	 */
	public static String calculateMD5AndCovertToBase64(String data) {
		String crypted=md5Crypt(data);
		String encoded=encodeToBase64(crypted);
		return encoded;
	}
	
	/**
	 * The method calculate the hash with SHA1 of the string "data". <br>
	 * And then encode the hash in base64
	 * @param data
	 * @return
	 */
	public static String calculateSHA1AndCovertToBase64(String data) {
		String crypted=sha1Crypt(data);
		String encoded=encodeToBase64(crypted);
		return encoded;
	}
	
	/**
	 * The method calculate the hash with SHA256 of the string "data". <br>
	 * And then encode the hash in base64
	 * @param data
	 * @return
	 */
	public static String calculateSHA256AndCovertToBase64(String data) {
		String crypted=sha256Crypt(data);
		String encoded=encodeToBase64(crypted);
		return encoded;
	}
	
	
}
