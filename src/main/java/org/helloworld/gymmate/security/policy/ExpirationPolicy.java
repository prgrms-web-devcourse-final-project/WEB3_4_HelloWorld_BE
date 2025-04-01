package org.helloworld.gymmate.security.policy;

import org.springframework.beans.factory.annotation.Value;

public class ExpirationPolicy {
    @Value("${jwt.access-token.expiration-time}")
    private static long ACCESS_TOKEN_EXPIRATION_TIME;
    @Value("${jwt.refresh-token.expiration-time}")
    private static long REFRESH_TOKEN_EXPIRATION_TIME;

    public static long getAccessTokenExpirationTime() {
        return ACCESS_TOKEN_EXPIRATION_TIME;
    }

    public static long getRefreshTokenExpirationTime() {
        return REFRESH_TOKEN_EXPIRATION_TIME;
    }

}
