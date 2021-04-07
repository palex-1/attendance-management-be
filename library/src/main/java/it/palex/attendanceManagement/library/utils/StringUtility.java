package it.palex.attendanceManagement.library.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtility {

	/**
	 * Checks if a CharSequence is empty ("") or null.<br>
		 StringUtility.isEmpty(null)      = true<br>
		 StringUtility.isEmpty("")        = true<br>
		 StringUtility.isEmpty(" ")       = true<br>
		 StringUtility.isEmpty("bob")     = false<br>
		 StringUtility.isEmpty("  bob  ") = false<br>
	 * @param str
	 * @return
	 */
	public static boolean isEmptyOrWhitespace(CharSequence str) {
		if(str==null) {
			return true;
		}
		return StringUtils.isWhitespace(str);
	}
	
	public static boolean isEmpty(CharSequence str) {
		return StringUtils.isEmpty(str);
	}
	
}
