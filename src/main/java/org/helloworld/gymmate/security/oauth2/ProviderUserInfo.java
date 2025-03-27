package org.helloworld.gymmate.security.oauth2;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProviderUserInfo {
	private Map<String, Object> attributes;

	public String getProviderId() {
		return attributes.get("id").toString();
	}
}
