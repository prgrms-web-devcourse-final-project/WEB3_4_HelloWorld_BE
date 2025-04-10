package org.helloworld.gymmate.common.cookie;

import java.util.Arrays;
import java.util.Optional;

import org.helloworld.gymmate.common.enums.TokenType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@RequestScope
public class CookieManager {
	private final HttpServletResponse response;
	private final HttpServletRequest request;

	public String getCookieByTokenType(TokenType tokenType) {
		return Optional.ofNullable(request.getCookies())
			.filter(cookies -> cookies.length > 0)
			.stream()
			.flatMap(Arrays::stream)
			.filter(cookie -> cookie.getName().equals(tokenType.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);
	}

	public void setCookie(TokenType tokenType, String token, long expiration) {
		ResponseCookie cookie = ResponseCookie.from(tokenType.getName(), token)
			.maxAge(expiration)
			.sameSite("None")
			.path("/")
			.secure(true)
			.httpOnly(true)
			.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}

	public void removeCookie(TokenType tokenType) {
		ResponseCookie cookie = ResponseCookie.from(tokenType.getName(), "")
			.maxAge(0)
			.sameSite("None")
			.path("/")
			.secure(true)
			.httpOnly(true)
			.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}
}
