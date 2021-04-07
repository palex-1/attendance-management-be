package it.palex.attendanceManagement.data.entities.converters;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Alessandro Pagliaro
 * This Converter will remove all spaces and carriage returns in string and finally will trim the string
 */
public class OneLineStringSanitizerConverter {

	public static String convertToDatabaseColumn(String attribute) {
		if(attribute==null) {
			return null;
		}
		String park = attribute.replaceAll("\\n", " ");
		park = park.replaceAll("\\r", " ");
		park = park.replaceAll("[ ]+"," "); 
		
		return StringUtils.trim(park);
	}
	
	
}
