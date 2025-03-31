package org.helloworld.gymmate.security.model;

import java.util.Map;

import org.helloworld.gymmate.domain.user.enums.UserType;

public class SocialUserInfo {
	private final Map<String, Object> attributes;
	private final Map<String, Object> properties;

	public SocialUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
		this.properties = (Map<String, Object>)attributes.get("properties");
	}

	public String getProviderId() {
		return attributes.get("id").toString();
	}

	public UserType getUserType() {
		return UserType.fromString(properties.getOrDefault("userType", "MEMBER").toString());
	}
}