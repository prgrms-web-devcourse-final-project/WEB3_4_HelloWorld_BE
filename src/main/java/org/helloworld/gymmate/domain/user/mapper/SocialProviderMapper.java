package org.helloworld.gymmate.domain.user.mapper;

import org.helloworld.gymmate.domain.user.enumerate.SocialProviderType;
import org.helloworld.gymmate.domain.user.model.SocialProvider;

public class SocialProviderMapper {

	public static SocialProvider toEntity(SocialProviderType socialProviderType, String providerId) {
		return SocialProvider.builder()
			.provider(socialProviderType)
			.providerId(providerId)
			.build();
	}
}
