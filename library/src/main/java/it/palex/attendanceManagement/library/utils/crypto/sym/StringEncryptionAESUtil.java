package it.palex.attendanceManagement.library.utils.crypto.sym;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class StringEncryptionAESUtil {

	private static SecretKeySpec buildKey(char[] myKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		final byte[] key = toBytes(myKey);
		final MessageDigest sha = MessageDigest.getInstance("SHA-256");
		final byte[] digested = sha.digest(key);
		final byte[] finalKey = Arrays.copyOf(digested, 16);
		return new SecretKeySpec(finalKey, "AES");
	}

	public static String encrypt(String strToEncrypt, char[] secret)
			throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		if (strToEncrypt == null || secret == null) {
			throw new NullPointerException();
		}
		SecretKeySpec secretKey = buildKey(secret);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	}

	public static String decrypt(String strToDecrypt, char[] secret)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		if (strToDecrypt == null || secret == null) {
			throw new NullPointerException();
		}
		SecretKeySpec secretKey = buildKey(secret);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	}

	private static byte[] toBytes(char[] chars) {
		final CharBuffer charBuffer = CharBuffer.wrap(chars);
		final ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data

		return bytes;
	}

	public static String decryptCBC(String strToDecrypt, char[] secret, byte[] initializationVector, byte[] salt)
			throws Exception {

		IvParameterSpec ivspec = new IvParameterSpec(initializationVector);

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(secret, salt, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
		return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	}

	public static String encryptCBC(String strToEncrypt, char[] secret, byte[] initializationVector, byte[] salt)
			throws Exception {
		IvParameterSpec ivspec = new IvParameterSpec(initializationVector);

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(secret, salt, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
		return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	}

}
