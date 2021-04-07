package it.palex.attendanceManagement.data.entities.converters;

import javax.persistence.AttributeConverter;

public class DoubleTwoDecimalFormatRoundConverter implements AttributeConverter<Double, Double> {

	@Override
	public Double convertToDatabaseColumn(Double attribute) {
		if(attribute==null) {
			return attribute;
		}
		double roundedFloat = Math.round(attribute * 100.0) / 100.0;
		
		return roundedFloat;
	}

	@Override
	public Double convertToEntityAttribute(Double dbData) {
		if(dbData==null) {
			return dbData;
		}
		double roundedFloat = Math.round(dbData * 100.0) / 100.0;
		
		return roundedFloat;
	}


}
