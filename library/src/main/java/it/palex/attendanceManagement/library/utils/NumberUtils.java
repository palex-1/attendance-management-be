package it.palex.attendanceManagement.library.utils;

public class NumberUtils {

	
	/**
	 * 
	 * @param number
	 * @return null if number is invalid otherwise the number
	 */
	public static Long tryToParseLong(String number) {
		try {
			return Long.parseLong(number);
		}catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param number
	 * @param defaultValue
	 * @return null if number is invalid otherwise the number
	 */
	public static long tryToParseLong(String number, long defaultValue) {
		try {
			return Long.parseLong(number);
		}catch(Exception e) {
			return defaultValue;
		}
	}
	
	
	public static boolean isAnIntegerString(String number) {
		try {
			Integer.parseInt(number);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	/**
	 * 
	 * @param number
	 * @return null if number is invalid otherwise the number
	 */
	public static Integer tryToParseInteger(String number) {
		try {
			return Integer.parseInt(number);
		}catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param number
	 * @param defaultValue
	 * @return defaultValue if number is invalid otherwise the number
	 */
	public static int tryToParseInteger(String number, int defaultValue) {
		try {
			return Integer.parseInt(number);
		}catch(Exception e) {
			return defaultValue;
		}
	}
	
	
}
