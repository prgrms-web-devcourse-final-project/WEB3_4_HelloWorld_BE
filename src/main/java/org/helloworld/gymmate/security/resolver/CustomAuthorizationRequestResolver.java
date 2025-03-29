package org.helloworld.gymmate.security.resolver;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
	private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

	public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
		this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
			"/oauth2/authorization");
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
		return customizeAuthorizationRequest(request, authorizationRequest);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
		return customizeAuthorizationRequest(request, authorizationRequest);
	}

	private OAuth2AuthorizationRequest customizeAuthorizationRequest(HttpServletRequest request,
		OAuth2AuthorizationRequest authorizationRequest) {
		if (authorizationRequest == null || request == null) {
			return null;
		}
		// 클라이언트 URL에서 state 파라미터를 읽음 (예: ?state=TRAINER)

		String paramValue = request.getParameter("param");
		Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());
		if (paramValue != null) {
			// 값은 대문자로 변환 (예: "TRAINER" 또는 "MEMBER")
			String userType = paramValue.toUpperCase();
			additionalParameters.put("userType", userType);
			// HTTP 세션에 저장
			request.getSession().setAttribute("userType", userType);
		}
		log.info("Detected userType: {}", paramValue);
		return OAuth2AuthorizationRequest.from(authorizationRequest)
			.additionalParameters(additionalParameters)
			.build();
	}
}
