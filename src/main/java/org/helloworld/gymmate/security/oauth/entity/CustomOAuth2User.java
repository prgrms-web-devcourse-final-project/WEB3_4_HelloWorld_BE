package org.helloworld.gymmate.security.oauth.entity;

import java.util.Collection;
import java.util.Map;

import org.helloworld.gymmate.domain.user.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;

@Getter
public class CustomOAuth2User implements OAuth2User {
	private final OAuth2User oAuth2User;
	private final Long userId;
	private final UserType userType; // MEMBER, TRAINER

	public CustomOAuth2User(OAuth2User oAuth2User, Long userId, UserType userType) {
		this.oAuth2User = oAuth2User;
		this.userId = userId;
		this.userType = userType;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oAuth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oAuth2User.getAuthorities();
	}

	@Override
	public String getName() {
		return oAuth2User.getName();
	}

}
