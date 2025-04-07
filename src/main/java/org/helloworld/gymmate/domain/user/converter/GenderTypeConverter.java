package org.helloworld.gymmate.domain.user.converter;

import org.helloworld.gymmate.domain.user.enums.GenderType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GenderTypeConverter implements AttributeConverter<GenderType, String> {

	@Override
	public String convertToDatabaseColumn(GenderType genderType) {
		return genderType != null ? genderType.getName() : null;
	}

	@Override
	public GenderType convertToEntityAttribute(String s) {
		return s != null ? GenderType.fromString(s) : null;
	}
}
