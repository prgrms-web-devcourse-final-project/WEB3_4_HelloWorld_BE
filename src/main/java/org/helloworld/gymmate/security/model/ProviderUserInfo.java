package org.helloworld.gymmate.security.model;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProviderUserInfo {
	private Map<String, Object> attributes;

	public String getProviderId() {
		return attributes.get("id").toString();
	}
}
