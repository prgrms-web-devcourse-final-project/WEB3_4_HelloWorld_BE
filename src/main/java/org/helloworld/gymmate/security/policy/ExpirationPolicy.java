package org.helloworld.gymmate.security.policy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExpirationPolicy {
    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;
    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    public long getAccessTokenExpirationTime() {
        return ACCESS_TOKEN_EXPIRATION_TIME;
    }

    public long getRefreshTokenExpirationTime() {
        return REFRESH_TOKEN_EXPIRATION_TIME;
    }

}
