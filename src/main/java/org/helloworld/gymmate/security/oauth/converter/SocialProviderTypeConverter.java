package org.helloworld.gymmate.security.oauth.converter;

import org.helloworld.gymmate.domain.user.enums.SocialProviderType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SocialProviderTypeConverter implements AttributeConverter<SocialProviderType, String> {

	@Override
	public String convertToDatabaseColumn(SocialProviderType socialProviderType) {
		return socialProviderType != null ? socialProviderType.getProviderName() : null;
	}

	@Override
	public SocialProviderType convertToEntityAttribute(String s) {
		return s != null ? SocialProviderType.fromProviderName(s) : null;
	}
}
