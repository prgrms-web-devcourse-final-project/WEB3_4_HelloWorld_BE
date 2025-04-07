package org.helloworld.gymmate.domain.user.converter;

import org.helloworld.gymmate.domain.user.enums.UserType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserTypeConverter implements AttributeConverter<UserType, String> {

	@Override
	public String convertToDatabaseColumn(UserType userType) {
		return userType != null ? userType.getUserType() : null;
	}

	@Override
	public UserType convertToEntityAttribute(String s) {
		return s != null ? UserType.fromString(s) : null;
	}
}