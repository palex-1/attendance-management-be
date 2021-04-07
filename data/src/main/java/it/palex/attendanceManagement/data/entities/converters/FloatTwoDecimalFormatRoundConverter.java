package it.palex.attendanceManagement.data.entities.converters;

import javax.persistence.AttributeConverter;

public class FloatTwoDecimalFormatRoundConverter implements AttributeConverter<Float, Float> {

	@Override
	public Float convertToDatabaseColumn(Float attribute) {
		if(attribute==null) {
			return attribute;
		}
		Double roundedFloat = (Math.round(attribute * 100.0) / 100.0);
		
		return roundedFloat.floatValue();
	}

	@Override
	public Float convertToEntityAttribute(Float dbData) {
		if(dbData==null) {
			return dbData;
		}
		Double roundedFloat = (Math.round(dbData * 100.0) / 100.0);
		
		return roundedFloat.floatValue();
	}


}
