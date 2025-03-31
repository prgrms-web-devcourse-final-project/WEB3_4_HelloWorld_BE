package org.helloworld.gymmate.security.handler;

import java.io.IOException;

import org.helloworld.gymmate.common.cookie.CookieManager;
import org.helloworld.gymmate.common.enums.TokenType;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
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
	private final TrainerService trainerService;
	private final MemberService memberService;
	@Value("${jwt.redirect}")
	private String REDIRECT_URI; // 프론트엔드로 Jwt 토큰을 리다이렉트할 URI

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication; // 토큰
		CustomOAuth2User customOAuth2User = (CustomOAuth2User)token.getPrincipal();

		if (customOAuth2User.getUserType() == UserType.MEMBER) {
			Member member = memberService.findByUserId(customOAuth2User.getUserId());
			log.debug("Member 생성 확인: {}", member);
			if (!member.getAdditionalInfoCompleted()) { // 추가 정보 입력이 필요한 경우
				// 회원 추가 정보 입력 페이지로 리다이렉트할 URI
				String memberRedirectUri = "http://localhost:3000/login?additionalInfoCompleted=false&role=member"; // 예시(회원 추가 정보 입력 페이지)
				registerTokens(request, response, customOAuth2User, memberRedirectUri); // 추가 정보 입력 페이지로 리다이렉트
				return; // 리다이렉트 후 더 이상 진행하지 않도록 return 처리
			}

		}

		if (customOAuth2User.getUserType() == UserType.TRAINER) {
			Trainer trainer = trainerService.findByUserId(customOAuth2User.getUserId());
			if (!trainer.getAdditionalInfoCompleted()) { // 추가 정보 입력이 필요한 경우
				// 트레이너 추가 정보 입력 페이지로 리다이렉트할 URI
				String trainerRedirectUri = "http://localhost:3000/login?additionalInfoCompleted=false&role=trainer";// 예시(트레이너 추가 정보 입력 페이지)
				registerTokens(request, response, customOAuth2User, trainerRedirectUri); // 추가 정보 입력 페이지로 리다이렉트
				return;  // 리다이렉트 후 더 이상 진행하지 않도록 return 처리
			}
		}
		String mainRedirectUri = "http://localhost:3000"; // 예시(메인페이지)
		registerTokens(request, response, customOAuth2User, mainRedirectUri); // 메인 페이지로 리다이렉트 (추가 정보 입력 완료)
	}

	private void registerTokens(HttpServletRequest request, HttpServletResponse response,
		CustomOAuth2User customUser, String redirectUri) throws
		IOException {
		String refreshToken = jwtManager.createRefreshToken(customUser.getUserId(),
			customUser.getUserType().toString());
		String accessToken = jwtManager.createAccessToken(customUser.getUserId(), customUser.getUserType().toString());

		cookieManager.setCookie(TokenType.ACCESS_TOKEN, accessToken,
			ExpirationPolicy.getAccessTokenExpirationTime());
		cookieManager.setCookie(TokenType.REFRESH_TOKEN, refreshToken,
			ExpirationPolicy.getRefreshTokenExpirationTime());

		getRedirectStrategy().sendRedirect(request, response, redirectUri);
	}

}
