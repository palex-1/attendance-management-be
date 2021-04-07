package it.palex.attendanceManagement.data.entities.converters;

import org.apache.commons.lang3.StringUtils;

public class FileNameSanitizeConverter {

	private static final char[] ILLEGAL_CHARS = {'#', '%', '&', '{', '}', 
			'<', '>', '*', '?', '/', '\\', '$', '!', ',', '\'', ':', '@' };
	
	private static final char REPLACED_CHAR = '_';
	
	public static String convertToDatabaseColumn(String attribute) {
		if(attribute==null) {
			return null;
		}
		String park = attribute.replaceAll("\\n", " "); // delete all new line char
		park = park.replaceAll("\\r", " "); // delete all cariage return char
		park = park.replaceAll("[ ]+"," "); //replace multiple spaces with one space
		park = park.replaceAll("\\s+",""); //delete all spaces
		
		
		for(int i=0; i<ILLEGAL_CHARS.length; i++) {
			park = StringUtils.replaceChars(park, ILLEGAL_CHARS[i], REPLACED_CHAR);
		}
		
		return StringUtils.trim(park);
	}

	
}
