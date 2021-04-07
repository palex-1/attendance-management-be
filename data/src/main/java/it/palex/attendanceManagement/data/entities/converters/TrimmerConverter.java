package it.palex.attendanceManagement.data.entities.converters;

import javax.persistence.AttributeConverter;

import org.apache.commons.lang3.StringUtils;

public class TrimmerConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String attribute) {
		return StringUtils.trim(attribute);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		return dbData;
	}

}
