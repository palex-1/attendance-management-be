package it.palex.attendanceManagement.data.entities.converters;

import org.apache.commons.lang3.StringUtils;

public class ToUpperCaseAndTrimmerSpaceReplacerConverter {

	public static String convertToDatabaseColumn(String attribute) {
		if(attribute==null) {
			return null;
		}
		String park = attribute.replaceAll("[ ]+"," "); 
		
		return StringUtils.trim(park).toUpperCase();
	}


}
