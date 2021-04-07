package it.palex.attendanceManagement.data.entities.converters;

import org.apache.commons.lang3.StringUtils;

public class ToUpperCaseConverter {

	public static String convertToDatabaseColumn(String attribute) {
		String uppercased = StringUtils.upperCase(attribute);
		return uppercased;
	}



}
