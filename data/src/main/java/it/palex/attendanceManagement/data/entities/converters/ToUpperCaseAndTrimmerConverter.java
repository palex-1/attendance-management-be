package it.palex.attendanceManagement.data.entities.converters;

import javax.persistence.AttributeConverter;

import org.apache.commons.lang3.StringUtils;

public class ToUpperCaseAndTrimmerConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if(attribute==null) {
			return null;
		}
		
		return StringUtils.trim(attribute).toUpperCase();
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		return dbData;
	}


}
