package org.helloworld.gymmate.security.handler;

import java.io.IOException;

import org.helloworld.gymmate.common.cookie.CookieManager;
import org.helloworld.gymmate.common.enums.TokenType;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.helloworld.gymmate.security.oauth.service.OauthService;
import org.helloworld.gymmate.security.policy.ExpirationPolicy;
import org.helloworld.gymmate.security.token.JwtManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final CookieManager cookieManager;
	private final JwtManager jwtManager;
	private final OauthService oauthService;

	@Value("${jwt.redirect}")
	private String REDIRECT_URI; // 프론트엔드로 Jwt 토큰을 리다이렉트할 URI

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication; // 토큰
		Object principal = token.getPrincipal();

		if (principal instanceof CustomOAuth2User customUser) {
			registerTokens(request, response, customUser);
		} else {
			throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
		}
	}

	private void registerTokens(HttpServletRequest request, HttpServletResponse response,
		CustomOAuth2User customUser) throws
		IOException {
		String refreshToken = jwtManager.createRefreshToken(customUser.getUserId(),
			customUser.getUserType().toString());
		String accessToken = jwtManager.createAccessToken(customUser.getUserId(), customUser.getUserType().toString());

		cookieManager.setCookie(TokenType.ACCESS_TOKEN, accessToken,
			ExpirationPolicy.getAccessTokenExpirationTime());
		cookieManager.setCookie(TokenType.REFRESH_TOKEN, refreshToken,
			ExpirationPolicy.getRefreshTokenExpirationTime());

		getRedirectStrategy().sendRedirect(request, response, String.format(REDIRECT_URI));
	}

	private Long getLoginUserId(OAuth2AuthenticationToken token) {
		Object principal = token.getPrincipal();
		if (principal instanceof CustomOAuth2User customUser) {
			log.debug("유저 ID, 유저 Type: {}, {}", customUser.getUserId(), customUser.getUserType());
			return customUser.getUserId();
		}
		throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
	}
}
