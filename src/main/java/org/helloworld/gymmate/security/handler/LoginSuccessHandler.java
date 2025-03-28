package org.helloworld.gymmate.security.handler;

import java.io.IOException;
import java.util.Map;

import org.helloworld.gymmate.common.cookie.CookieManager;
import org.helloworld.gymmate.common.enums.TokenType;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.security.model.SocialUserInfo;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
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
		// 카카오로부터 받은 전체 사용자 정보를 Map 형태로 가져옴
		Map<String, Object> attributes = token.getPrincipal().getAttributes();

		// 정보를 담을 임시 객체 생성
		SocialUserInfo socialUserInfo = new SocialUserInfo(attributes);

		// 필요한 정보 가져오기
		String providerId = socialUserInfo.getProviderId();
		UserType userType = socialUserInfo.getUserType();

		log.debug("PROVIDER : {}", provider);
		log.debug("PROVIDER_ID : {}", providerId);

		// oauth가 없는 경우 -> oauth 생성
		Oauth oauth = getOauth(providerId);
		// oauth는 있는데, member or trainer 가 없는 경우 -> member or trainer 생성
		return getUserId(oauth, userType);
	}

	private Oauth getOauth(String providerId) {
		return oauthService.findOauthByProviderId(providerId).orElseGet(() -> oauthService.createOauth(providerId));
	}

	private Long getUserId(Oauth oauth, UserType userType) {
		Long userId = 0L;

		if (userType == UserType.MEMBER) {
			//to do: userId = memberService.getMemberByOauth(oauth);
			log.debug("기존 유저입니다.");
			//존재하지 않으면
			log.debug("신규 유저입니다. 등록을 진행합니다.");
			//to do: memberService.createMember(SocialProviderType.fromProviderName(provider), providerId);
		}

		if (userType == UserType.TRAINER) {
			//to do: userId = trainerService.getTrainerByOauth(oauth);
			log.debug("기존 유저입니다.");
			//존재하지 않으면
			log.debug("신규 유저입니다. 등록을 진행합니다.");
			//to do: trainerService.createTrainer(SocialProviderType.fromProviderName(provider), providerId);
		}

		return userId;
	}
}
