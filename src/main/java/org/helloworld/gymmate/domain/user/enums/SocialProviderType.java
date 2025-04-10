package org.helloworld.gymmate.domain.user.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialProviderType {
    KAKAO("kakao"),
    GOOGLE("google");

    private final String providerName;

    public static SocialProviderType fromProviderName(String provider) {
        return Arrays.stream(SocialProviderType.values())
            .filter(type -> type.getProviderName().equalsIgnoreCase(provider))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 로그인 제공자: " + provider));
    }
}
