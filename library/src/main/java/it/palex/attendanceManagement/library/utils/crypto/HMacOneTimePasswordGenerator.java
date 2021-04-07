package it.palex.attendanceManagement.library.utils.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class contains static methods that are used to calculate the One-Time
 * Password (OTP) using JCE to provide the HMAC-SHA-1.
 *
 * @author Loren Hart
 * @version 1.0
 */
public class HMacOneTimePasswordGenerator {
	
	private static long MOVING_FACTOR = ThreadLocalRandom.current().nextLong();
	
	// These are used to calculate the check-sum digits.
	// 0 1 2 3 4 5 6 7 8 9
	private static final int[] doubleDigits = { 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 };
	
	private static final int[] DIGITS_POWER
	// 0 1 2 3 4 5 6 7 8
			= { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };
	
	public static final int DEFAULT_TRUNCATION_OFFSET = 4;
	public static final int DEFAULT_CODE_DIGITS = 8;
	public static final int SECRET_MIN_LENGTH = 6;
	public static final int MIN_CODE_DIGIT_SIZE = 4;
	public static final int MAX_CODE_DIGIT_SIZE = 8;
	
	
	private final int truncationOffset;
	private final boolean addChecksum;
	private final int codeDigits;
	private long movingFactor;
	
	
	public HMacOneTimePasswordGenerator() {
		this(DEFAULT_CODE_DIGITS);
	}
	/**
	 * 
	 * @param secret
	 * @param codeDigits
	 * In this default method the checksum is not added
	 */
	public HMacOneTimePasswordGenerator(int codeDigits) {
		this(codeDigits, false);
	}
	
	/**
	 * 
	 * @param secret the secret to generate otp
	 * @param codeDigits the length of otp
	 * @param addChecksum to add checksum in the otp<br>
	 * Note: If <strong>addChecksum</strong> is true the generateOTP will return a code with length 
	 * <strong>codeDigits + 1</strong>
	 */
	public HMacOneTimePasswordGenerator(int codeDigits, boolean addChecksum) {
		this(codeDigits, addChecksum, DEFAULT_TRUNCATION_OFFSET);
	}
	
	public HMacOneTimePasswordGenerator(int codeDigits, 
											boolean addChecksum, int truncationOffset) {		
		this(codeDigits, addChecksum, DEFAULT_TRUNCATION_OFFSET, Math.abs(MOVING_FACTOR));
	}
	
	public HMacOneTimePasswordGenerator(int codeDigits, 
			boolean addChecksum, int truncationOffset, long movingFactor) {
	    if(truncationOffset<0) {
	    	throw new IllegalArgumentException("Truncation offset cannot be less that zero");
	    }
	    if(movingFactor<0) {
	    	throw new IllegalArgumentException("Moving factor cannot be less that zero");
	    }
	    if(codeDigits<MIN_CODE_DIGIT_SIZE || codeDigits>MAX_CODE_DIGIT_SIZE) {
	    	throw new IllegalArgumentException("codeDigits must be int the range of 4 to 8");
	    }
	    
		
		this.codeDigits = codeDigits;
		this.addChecksum = addChecksum;
		this.truncationOffset = truncationOffset;
		this.movingFactor = generateRandomMovingFactor();
    }
	
	private static long generateRandomMovingFactor() {
		MOVING_FACTOR++;
		SecureRandom rand = new SecureRandom();
		
		final long park =  rand.nextLong() * MOVING_FACTOR;
		
		return Math.abs(park);
	}

	public String generateOTP(String secret) throws InvalidKeyException, NoSuchAlgorithmException {
		if(secret==null) {
			throw new NullPointerException();
		}
		if(secret.length()<SECRET_MIN_LENGTH) {
	    	throw new IllegalArgumentException("Secret is too short");
	    }
		this.movingFactor = generateRandomMovingFactor();
			
		return generateOTP(secret.getBytes(), this.movingFactor, this.codeDigits, 
				this.addChecksum, this.truncationOffset);
	}
	
	
	/**
	 * Calculates the checksum using the credit card algorithm. This algorithm has
	 * the advantage that it detects any single mistyped digit and any single
	 * transposition of adjacent digits.
	 *
	 * @param num    the number to calculate the checksum for
	 * @param digits number of significant places in the number
	 *
	 * @return the checksum of num
	 */
	private static int calcChecksum(long num, int digits) {
		boolean doubleDigit = true;
		int total = 0;
		while (0 < digits--) {
			int digit = (int) (num % 10);
			num /= 10;
			if (doubleDigit) {
				digit = doubleDigits[digit];
			}
			total += digit;
			doubleDigit = !doubleDigit;
		}
		int result = total % 10;
		if (result > 0) {
			result = 10 - result;
		}
		return result;
	}

	private static byte[] hmac_sha256(byte[] keyBytes, byte[] text)
			throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException {
		// try {
		Mac hmacSha1;
		try {
			hmacSha1 = Mac.getInstance("HmacSHA1");
		} catch (NoSuchAlgorithmException nsae) {
			hmacSha1 = Mac.getInstance("HMAC-SHA-1");
		}
		SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
		hmacSha1.init(macKey);
		return hmacSha1.doFinal(text);
		// } catch (GeneralSecurityException gse) {
		// throw new UndeclaredThrowableException(gse);
		// }
	}

	
	/**
	 * This method generates an OTP value for the given set of parameters.
	 *
	 * @param secret           the shared secret
	 * @param movingFactor     the counter, time, or other value that changes on a
	 *                         per use basis.
	 * @param codeDigits       the number of digits in the OTP, not including the
	 *                         checksum, if any.
	 * @param addChecksum      a flag that indicates if a checksum digit
	 * 
	 * 
	 * 
	 *                         M'Raihi, et al. Informational [Page 29]
	 * 
	 *                         RFC 4226 HOTP Algorithm December 2005
	 * 
	 * 
	 *                         should be appended to the OTP.
	 * @param truncationOffset the offset into the MAC result to begin truncation.
	 *                         If this value is out of the range of 0 ... 15, then
	 *                         dynamic truncation will be used. Dynamic truncation
	 *                         is when the last 4 bits of the last byte of the MAC
	 *                         are used to determine the start offset.
	 * @throws NoSuchAlgorithmException if no provider makes either HmacSHA1 or
	 *                                  HMAC-SHA-1 digest algorithms available.
	 * @throws InvalidKeyException      The secret provided was not a valid
	 *                                  HMAC-SHA-1 key.
	 *
	 * @return A numeric String in base 10 that includes {@link codeDigits} digits
	 *         plus the optional checksum digit if requested.
	 */
	private static String generateOTP(byte[] secret, long movingFactor, int codeDigits, boolean addChecksum,
			int truncationOffset) throws NoSuchAlgorithmException, InvalidKeyException {
		// put movingFactor value into text byte array
		
		String result = null;
		int digits = addChecksum ? (codeDigits + 1) : codeDigits;
		byte[] text = new byte[8];
		for (int i = text.length - 1; i >= 0; i--) {
			text[i] = (byte) (movingFactor & 0xff);
			movingFactor >>= 8;
		}

		// compute hmac hash
		byte[] hash = hmac_sha256(secret, text);

		// put selected bytes into result int
		int offset = hash[hash.length - 1] & 0xf;
		if ((0 <= truncationOffset) && (truncationOffset < (hash.length - 4))) {
			offset = truncationOffset;
		}
		int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16)
				| ((hash[offset + 2] & 0xff) << 8)

				| (hash[offset + 3] & 0xff);

		int otp = binary % DIGITS_POWER[codeDigits];
		if (addChecksum) {
			otp = (otp * 10) + calcChecksum(otp, codeDigits);
		}
		result = Integer.toString(otp);
		while (result.length() < digits) {
			result = "0" + result;
		}
		return result;
	}

}
