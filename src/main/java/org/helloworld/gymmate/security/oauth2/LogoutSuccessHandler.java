package org.helloworld.gymmate.security.oauth2;

import java.util.concurrent.TimeUnit;

import org.helloworld.gymmate.common.enums.TokenType;
import org.helloworld.gymmate.security.token.JwtManager;
import org.helloworld.gymmate.common.cookie.CookieManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutSuccessHandler implements LogoutHandler {

	private final CookieManager cookieManager;
	private final RedisTemplate<String, String> redisTemplate;
	private final JwtManager jwtManager;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		// access-token 쿠키 삭제
		cookieManager.removeCookie(TokenType.ACCESS_TOKEN);

		// refresh-token 쿠키 삭제
		String refreshToken = cookieManager.getCookieByTokenType(TokenType.REFRESH_TOKEN);
		redisTemplate.opsForValue()
			.set(refreshToken, "logout", jwtManager.getExpirationTime(refreshToken), TimeUnit.MILLISECONDS);
		cookieManager.removeCookie(TokenType.REFRESH_TOKEN);

		// JSESSIONID 쿠키 삭제
		cookieManager.removeCookie(TokenType.JSESSIONID);
	}
}
