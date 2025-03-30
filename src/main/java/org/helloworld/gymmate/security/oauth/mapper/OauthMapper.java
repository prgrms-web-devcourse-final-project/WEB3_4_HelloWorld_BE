package org.helloworld.gymmate.security.oauth.mapper;

import org.helloworld.gymmate.domain.user.enums.SocialProviderType;
import org.helloworld.gymmate.security.oauth.entity.Oauth;

public class OauthMapper {

	public static Oauth toEntity(SocialProviderType socialProviderType, String providerId) {
		return Oauth.builder()
			.provider(socialProviderType)
			.providerId(providerId)
			.build();
	}
}
