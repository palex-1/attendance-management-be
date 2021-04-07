package it.palex.attendanceManagement.library.utils.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alessandro Pagliaro
 *
 */
public class SecureStringGenerator {

	private static SecureStringGenerator generator = null;

	private ArrayList<Integer> letterListAscii = null;
	private ArrayList<Integer> letterListWithoutSimilarChar = null;
	private ArrayList<Integer> numberListAscii = null;
	private ArrayList<Integer> numberListAsciiWithoutSimilarChar = null;
	
	private ArrayList<Integer> specialCharacterListAscii = null;
	private ArrayList<Integer> numberCharacterAndLessSpecialCharacterASCII = null;
	private static Object lock = new Object();

	private SecureStringGenerator() {
		this.buildLetterListAscii();
		this.buildLetterListAsciiWithoutSimilar();
		this.buildNumberListAscii();
		this.buildNumberListAsciiWithoutSimilarChar();
		
		this.buildSpecialCharactersListAscii();
		this.buildNumberCharacterAndLessSpecialCharacterASCII();
	}

	

	public static SecureStringGenerator getInstance() {
		if (generator == null) {
			synchronized (lock) {
				generator = new SecureStringGenerator();
			}
		}
		return generator;
	}

	public String generateSecureLettersCasualString(int targetStringLength, boolean removeSimilarChars) {
		if (targetStringLength <= 0) {
			throw new IllegalArgumentException();
		}
		if(removeSimilarChars) {
			Collections.shuffle(this.letterListWithoutSimilarChar);
			return generateSecureLettersCasualString(this.letterListWithoutSimilarChar, targetStringLength);
		}
		
		Collections.shuffle(this.letterListAscii);
		return generateSecureLettersCasualString(this.letterListAscii, targetStringLength);
	}

	public String generateSecureNumericCasualString(int targetStringLength, boolean removeSimilarChars) {
		if (targetStringLength <= 0) {
			throw new IllegalArgumentException();
		}
		
		if(removeSimilarChars) {
			Collections.shuffle(this.numberListAsciiWithoutSimilarChar);
			return generateSecureLettersCasualString(this.numberListAsciiWithoutSimilarChar, targetStringLength);
		}
		
		Collections.shuffle(this.numberListAscii);
		return generateSecureLettersCasualString(this.numberListAscii, targetStringLength);
	}

	public String generateSecureSpecialCharacterCasualString(int targetStringLength) {
		if (targetStringLength <= 0) {
			throw new IllegalArgumentException();
		}
		Collections.shuffle(this.specialCharacterListAscii);
		return generateSecureLettersCasualString(this.specialCharacterListAscii, targetStringLength);
	}

	public String generateSecureCharacterNumericAndLESSpecialCharacters(int targetStringLength) {
		if (targetStringLength <= 1) {
			throw new IllegalArgumentException();
		}
		Collections.shuffle(this.numberCharacterAndLessSpecialCharacterASCII);
		return generateSecureLettersCasualString(this.numberCharacterAndLessSpecialCharacterASCII, targetStringLength);

	}

	public String generateSecureCharacterAndNumeric(int targetStringLength, boolean removeSimilarChars) {
		if (targetStringLength <= 1) {
			throw new IllegalArgumentException();
		}
		int lenghtChar = targetStringLength / 2;
		int lenghtNum = targetStringLength - lenghtChar;

		List<String> s1 = Arrays.asList(generateSecureLettersCasualString(lenghtChar, removeSimilarChars).split(""));
		List<String> s2 = Arrays.asList(generateSecureNumericCasualString(lenghtNum, removeSimilarChars).split(""));

		return generateStringShufflingList(s1, s2);
	}

	public String generateSecureCharacterAndSpecialCharacters(int targetStringLength) {
		if (targetStringLength <= 1) {
			throw new IllegalArgumentException();
		}
		int lenghtChar = targetStringLength / 2;
		int lenghtSpec = targetStringLength - lenghtChar;

		List<String> s1 = Arrays.asList(generateSecureLettersCasualString(lenghtChar, false).split(""));
		List<String> s2 = Arrays.asList(generateSecureSpecialCharacterCasualString(lenghtSpec).split(""));

		return generateStringShufflingList(s1, s2);

	}
	
	public String generateSecureCharacterNumericAndSpecialCharacters(int targetStringLength) {
		if (targetStringLength <= 2) {
			throw new IllegalArgumentException();
		}
		int lenghtNum = targetStringLength / 3;
		int lenghtSpec = targetStringLength / 3;
		int lenghtChar = targetStringLength - lenghtNum - lenghtSpec;

		List<String> park = Arrays.asList(generateSecureCharacterAndNumeric(lenghtNum + lenghtChar, false).split(""));

		List<String> s3 = Arrays.asList(generateSecureSpecialCharacterCasualString(lenghtSpec).split(""));

		return generateStringShufflingList(park, s3);
	}

	public String generateSecureNumericAndSpecialCharacters(int targetStringLength) {
		if (targetStringLength <= 1) {
			throw new IllegalArgumentException();
		}
		int lenghtSpec = targetStringLength / 2;
		int lenghtNum = targetStringLength - lenghtSpec;

		List<String> s1 = Arrays.asList(generateSecureNumericCasualString(lenghtNum, false).split(""));
		List<String> s2 = Arrays.asList(generateSecureSpecialCharacterCasualString(lenghtSpec).split(""));

		return generateStringShufflingList(s1, s2);
	}

	public String generateStringShufflingList(List<String> s1, List<String> s2) {
		if (s1 == null || s2 == null) {
			throw new NullPointerException();
		}
		List<String> sum = new LinkedList<String>();
		sum.addAll(s1);
		sum.addAll(s2);

		Collections.shuffle(sum);

		StringBuilder res = new StringBuilder(sum.size());

		for (String s : sum) {
			res.append(s);
		}

		return res.toString();
	}

	private static String generateSecureLettersCasualString(ArrayList<Integer> list, int targetStringLength) {
		if (list == null) {
			throw new NullPointerException();
		}
		if (list.isEmpty() || targetStringLength <= 0) {
			throw new IllegalArgumentException();
		}

		int min = 0;
		int max = list.size() - 1;

		SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		}


		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = min + (int) (random.nextFloat() * (max - min + 1));
			buffer.append((char) list.get(randomLimitedInt).intValue());
		}
		String generatedString = buffer.toString();

		return generatedString;
	}

	/**
	 * This method will build the specialCharacterListAscii adding ascii code from
	 * 65 to 90 and from 97 to 122
	 */
	private void buildLetterListAscii() {
		this.letterListAscii = new ArrayList<Integer>(50);
		this.letterListAscii.addAll(this.buildListFromTo(65, 90));
		this.letterListAscii.addAll(this.buildListFromTo(97, 122));
	}

	private void buildLetterListAsciiWithoutSimilar() {
		this.letterListWithoutSimilarChar = new ArrayList<Integer>(50);
		
		//skip similar chars
		final Integer CAPITAL_I = 73;
		final Integer CAPITAL_O = 79;
		this.letterListWithoutSimilarChar.addAll(this.buildListFromTo(65, 90));
		this.letterListWithoutSimilarChar.remove(CAPITAL_I);
		this.letterListWithoutSimilarChar.remove(CAPITAL_O);
		
		
		final Integer LOWER_L = 108;
		final Integer LOWER_O = 111;
		final Integer LOWER_I = 105;
		//skip similar chars
		this.letterListWithoutSimilarChar.addAll(this.buildListFromTo(97, 122));
		this.letterListWithoutSimilarChar.remove(LOWER_L);
		this.letterListWithoutSimilarChar.remove(LOWER_O);
		this.letterListWithoutSimilarChar.remove(LOWER_I);
		
	}
	
	/**
	 * This method will build the specialCharacterListAscii adding ascii code from
	 * 48 to 57
	 */
	private void buildNumberListAscii() {
		this.numberListAscii = new ArrayList<Integer>(12);
		this.numberListAscii.addAll(this.buildListFromTo(48, 57));
	}

	private void buildNumberListAsciiWithoutSimilarChar() {
		this.numberListAsciiWithoutSimilarChar = new ArrayList<Integer>(12);
		this.numberListAsciiWithoutSimilarChar.addAll(this.buildListFromTo(50, 57));
		
//		this.numberListAsciiWithoutSimilarChar.addAll(this.buildListFromTo(48, 57));
//		final Integer ZERO = 48;
//		final Integer ONE = 49;
//		this.numberListAsciiWithoutSimilarChar.remove(ZERO);
//		this.numberListAsciiWithoutSimilarChar.remove(ONE);
	}
	
	/**
	 * This method will build the specialCharacterListAscii adding ascii code from
	 * 33 to 47, from 58 to 64, from 91 to 96, from 123 to 125
	 */
	private void buildSpecialCharactersListAscii() {
		this.specialCharacterListAscii = new ArrayList<Integer>(40);
		this.specialCharacterListAscii.addAll(this.buildListFromTo(33, 47));
		this.specialCharacterListAscii.addAll(this.buildListFromTo(58, 64));
		this.specialCharacterListAscii.addAll(this.buildListFromTo(91, 96));
		this.specialCharacterListAscii.addAll(this.buildListFromTo(123, 125));
	}

	/**
	 * This method will build the specialCharacterListAscii adding ascii code from
	 * 48 to 59 and 61 to 125
	 */
	private void buildNumberCharacterAndLessSpecialCharacterASCII() {
		this.numberCharacterAndLessSpecialCharacterASCII = new ArrayList<Integer>(70);
		this.numberCharacterAndLessSpecialCharacterASCII.addAll(this.buildListFromTo(48, 59));
		this.numberCharacterAndLessSpecialCharacterASCII.addAll(this.buildListFromTo(61, 125));
	}

	/**
	 * 
	 * @param from INCLUSIVE
	 * @param to INCLUSIVE
	 * @return ann ArrayList of Integers
	 */
	private ArrayList<Integer> buildListFromTo(int from, int to) {
		if (from < 0 || from >= to) {
			throw new IllegalArgumentException();
		}
		ArrayList<Integer> list = new ArrayList<Integer>(to - from);

		for (int i = from; i <= to; i++) {
			list.add(i);
		}

		return list;
	}

}
