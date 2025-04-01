package org.helloworld.gymmate.security.oauth.service;

import java.util.Optional;

import org.helloworld.gymmate.domain.user.enums.SocialProviderType;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.helloworld.gymmate.security.oauth.repository.OauthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthService {
	private final OauthRepository oauthRepository;

	@Transactional(readOnly = true)
	public Optional<Oauth> findOauthByProviderIdAndUserType(String providerId, UserType userType) {
		return oauthRepository.findByProviderIdAndUserType(providerId, userType);
	}

	public Oauth createOauth(String providerId, UserType userType) {
		Oauth oauth = Oauth.builder()
			.provider(SocialProviderType.KAKAO) // to do: 다른 소셜 로그인 구현
			.providerId(providerId)
			.userType(userType)
			.build();
		oauthRepository.save(oauth);
		return oauth;
	}
}
