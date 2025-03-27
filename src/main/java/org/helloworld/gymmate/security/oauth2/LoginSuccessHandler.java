package org.helloworld.gymmate.security.oauth2;

import java.io.IOException;
import java.util.Optional;

import org.helloworld.gymmate.common.cookie.CookieManager;
import org.helloworld.gymmate.common.enums.TokenType;
import org.helloworld.gymmate.domain.user.member.service.GymmateUserService;
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
	private final GymmateUserService gymmateUserService;

	@Value("${jwt.redirect}")
	private String REDIRECT_URI; // 프론트엔드로 Jwt 토큰을 리다이렉트할 URI

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication; // 토큰
		String provider = token.getAuthorizedClientRegistrationId(); // provider 추출

		registerTokens(request, response, getLoginUserId(token, provider));
	}

	private void registerTokens(HttpServletRequest request, HttpServletResponse response, Long userId) throws
		IOException {
		String refreshToken = jwtManager.createRefreshToken(userId);
		String accessToken = jwtManager.createAccessToken(userId);

		cookieManager.setCookie(TokenType.ACCESS_TOKEN, accessToken,
			ExpirationPolicy.getAccessTokenExpirationTime());
		cookieManager.setCookie(TokenType.REFRESH_TOKEN, refreshToken,
			ExpirationPolicy.getRefreshTokenExpirationTime());

		getRedirectStrategy().sendRedirect(request, response, String.format(REDIRECT_URI));
	}

	private Long getLoginUserId(OAuth2AuthenticationToken token, String provider) {
		String providerId = new ProviderUserInfo(token.getPrincipal().getAttributes()).getProviderId();

		Optional<Long> existUserId = gymmateUserService.existSocialProvider(providerId);

		log.debug("PROVIDER : {}", provider);
		log.debug("PROVIDER_ID : {}", providerId);

		if (existUserId.isPresent()) {
			log.debug("기존 유저입니다.");
			return existUserId.get(); // 기존 유저인 경우
		}

		log.debug("신규 유저입니다. 등록을 진행합니다.");
		return null;
		//return gymmateUserService.createUser(SocialProviderType.fromProviderName(provider), providerId); // 신규 유저인 경우
	}
}
