package it.palex.attendanceManagement.library.utils.crypto.asym;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public class RSAPairKeyGenerator {

	public static final int KEY_BIT_LENGTH_RSA_MINIMAL = 1024;
	public static final int KEY_BIT_LENGTH_RSA_MEDIUM = 1536;
	public static final int KEY_BIT_LENGTH_RSA_STRONG = 2048;

	/**
	 * 
	 * @return an RSA KeyPair of 1024 bit length
	 */
	public static KeyPair generateKeyPair() {
		return generateKeyPair(KEY_BIT_LENGTH_RSA_MINIMAL);
	}

	/**
	 * 
	 * @return an RSA KeyPair
	 * @throws IllegalArgumentException if keyLength is not valid<br>
	 *                                  Only 1024, 1536 and 2048 key length are
	 *                                  supported
	 */
	public static KeyPair generateKeyPair(int keyLength) {
		if (keyLength != KEY_BIT_LENGTH_RSA_MINIMAL && keyLength != KEY_BIT_LENGTH_RSA_STRONG
				&& keyLength != KEY_BIT_LENGTH_RSA_MEDIUM) {
			throw new IllegalArgumentException();
		}

		KeyPairGenerator keyGenerator;
		try {
			keyGenerator = KeyPairGenerator.getInstance("RSA");
			keyGenerator.initialize(keyLength);

			return keyGenerator.generateKeyPair();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(
					"This cannot happen: Every implementation of the Java platform is required to support the following standard KeyPairGenerator algorithms");
		}

	}

	/*
	 * public static void main(String...args) throws InvalidKeyException, Exception{
	 * KeyPair pair =
	 * RSAPairKeyGenerator.generateKeyPair(KEY_BIT_LENGTH_RSA_MINIMAL); PrivateKey
	 * prv = pair.getPrivate(); PublicKey pub = pair.getPublic(); String data=
	 * "Hello world";
	 * 
	 * System.out.println(prv.getEncoded().length*8);
	 * 
	 * System.out.println( RSAPairKeyGenerator.getPublicKeyEncodedInBase64(pub) );
	 * System.out.println(Base64.getEncoder().encodeToString((prv.getEncoded())).
	 * length());
	 * 
	 * byte[] signed = SHA256withRSAStringSigner.sign(data, prv); String
	 * base64Encoded = Base64.getEncoder().encodeToString(signed); String urlEncoder
	 * = URLEncoder.encode(new String(Base64.getEncoder().encodeToString(signed)),
	 * "UTF-8");
	 * 
	 * System.out.println(base64Encoded); System.out.println(urlEncoder.length());
	 * 
	 * String urlDecoded = URLDecoder.decode(urlEncoder, "UTF-8");
	 * 
	 * byte[] base64Decoded = Base64.getDecoder().decode(urlDecoded);
	 * 
	 * 
	 * boolean verified =
	 * SHA256withRSAStringVerifier.verifySignature(data.getBytes(), base64Decoded,
	 * pub);
	 * 
	 * System.out.println(verified);
	 * 
	 * boolean failedVerified =
	 * SHA256withRSAStringVerifier.verifySignature("Hello workd".getBytes(),
	 * base64Decoded, pub);
	 * 
	 * System.out.println(failedVerified);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 */

	/**
	 * 
	 * @param key
	 * @return the <strong>key</strong> in byte form
	 */
	public static byte[] getPrivateKeyEncodedInByte(PrivateKey key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return key.getEncoded();
	}

	/**
	 * 
	 * @param key
	 * @return PrivateKey encoded in base64
	 */
	public static String getPrivateKeyEncodedInBase64(PrivateKey key) {
		if (key == null) {
			throw new NullPointerException();
		}
		byte[] bytes = key.getEncoded();

		return Base64.getEncoder().encodeToString(bytes);
	}

	/**
	 * 
	 * @param key
	 * @return the <strong>key</strong> in byte form
	 */
	public static byte[] getPublicKeyEncodedInByte(PublicKey key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return key.getEncoded();
	}

	/**
	 * 
	 * @param key
	 * @return PublicKey encoded in base64
	 */
	public static String getPublicKeyEncodedInBase64(PublicKey key) {
		if (key == null) {
			throw new NullPointerException();
		}
		byte[] bytes = key.getEncoded();

		return Base64.getEncoder().encodeToString(bytes);
	}

	/**
	 * 
	 * @param publicKeyBase64Encoded public key encoded in base 64
	 * @return a PublicKey Object builded from a string rapresentation
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKeyFromEncodedBase64String(String publicKeyBase64Encoded)
			throws InvalidKeySpecException {
		if (publicKeyBase64Encoded == null) {
			throw new NullPointerException();
		}
		byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64Encoded);

		return getPublicKeyFromBytes(keyBytes);

	}

	/**
	 * 
	 * @param keyBytes
	 * @return
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKeyFromBytes(byte[] keyBytes) throws InvalidKeySpecException {
		if (keyBytes == null) {
			throw new NullPointerException();
		}
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(
					"This cannot happen: Every implementation of the Java platform is required to support the following standard KeyPairGenerator algorithms");
		}
		return kf.generatePublic(spec);
	}

	/**
	 * 
	 * @param publicKeyBase64Encoded
	 * @return PrivateKey builded with <strong>publicKeyBase64Encoded</strong>
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKeyFromEncodedBase64String(String publicKeyBase64Encoded)
			throws InvalidKeySpecException {
		if (publicKeyBase64Encoded == null) {
			throw new NullPointerException();
		}
		byte[] base64Decoded = Base64.getDecoder().decode(publicKeyBase64Encoded);

		return getPrivateKeyFromBytes(base64Decoded);
	}

	/**
	 * 
	 * @param keyBytes
	 * @return PrivateKey builded with <strong>keyBytes</strong>
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKeyFromBytes(byte[] keyBytes) throws InvalidKeySpecException {
		if (keyBytes == null) {
			throw new NullPointerException();
		}
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(
					"Broken JVM: Every implementation of the Java platform is required to support the following standard KeyPairGenerator algorithms");
		}
		return kf.generatePrivate(spec);
	}

}
