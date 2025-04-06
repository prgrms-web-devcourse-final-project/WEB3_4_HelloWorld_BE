package org.helloworld.gymmate.security.token;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.helloworld.gymmate.security.oauth.service.CustomOAuth2UserService;
import org.helloworld.gymmate.security.policy.ExpirationPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtManager {
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final ExpirationPolicy expirationPolicy;

    @Value("${jwt.secret}")
    private String secret;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        log.debug("JWT 보안 키가 성공적으로 생성되었습니다.");
    }

    public String createAccessToken(Long userId, String userType) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("userType", userType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationPolicy.getAccessTokenExpirationTime()))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long userId, String userType) {
        String refreshToken = Jwts.builder()
                .claim("userId", userId)
                .claim("userType", userType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationPolicy.getRefreshTokenExpirationTime()))
                .signWith(secretKey)
                .compact();

        redisTemplate.opsForValue()
                .set(refreshToken, String.valueOf(userId), expirationPolicy.getRefreshTokenExpirationTime(),
                        TimeUnit.MILLISECONDS);

        return refreshToken;
    }

    public Long getExpirationTime(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration().getTime();
        } catch (JwtException e) {
            log.error("인증 토큰이 유효하지 않거나 만료되었습니다: {}", e.getMessage());
            throw new BusinessException(ErrorCode.TOKEN_NOT_VALID);
        }
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return !isExpired(token);
        } catch (JwtException e) {
            log.error("인증 토큰 검증에 실패했습니다: {}", e.getMessage());
            return false;
        }
    }

    private boolean isExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

    public String[] recreateTokens(String refreshToken) {
        Long userId = getUserIdByToken(refreshToken);
        String userType = getUserTypeByToken(refreshToken);

        String newAccessToken = createAccessToken(userId, userType);
        String newRefreshToken = createRefreshToken(userId, userType);

        return new String[]{newAccessToken, newRefreshToken};
    }

    public Long getUserIdByToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("userId", Long.class);
        } catch (JwtException e) {
            log.error("인증 토큰에서 사용자 정보를 가져오는데 실패했습니다: {}", e.getMessage());
            throw new BusinessException(ErrorCode.TOKEN_NOT_VALID);
        }
    }

    public String getUserTypeByToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("userType", String.class);
        } catch (JwtException e) {
            log.error("인증 토큰에서 사용자 정보를 가져오는데 실패했습니다: {}", e.getMessage());
            throw new BusinessException(ErrorCode.TOKEN_NOT_VALID);
        }
    }

    public Authentication getAuthentication(String accessToken) {
        CustomOAuth2User customOAuth2User = customOAuth2UserService.loadUserByUserId(
                getUserIdByToken(accessToken), getUserTypeByToken(accessToken));
        return new UsernamePasswordAuthenticationToken(customOAuth2User, "",
                customOAuth2User.getAuthorities());  // 인증 객체 생성
    }
}
