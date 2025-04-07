package org.helloworld.gymmate.security.oauth.entity;

import org.helloworld.gymmate.common.entity.BaseEntity;
import org.helloworld.gymmate.domain.user.converter.UserTypeConverter;
import org.helloworld.gymmate.domain.user.enums.SocialProviderType;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.security.oauth.converter.SocialProviderTypeConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "oauth")
public class Oauth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_id")
    private Long oauthId;

    @Convert(converter = SocialProviderTypeConverter.class)
    @Column(name = "provider_type", nullable = false)
    private SocialProviderType provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Convert(converter = UserTypeConverter.class)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    // ====== Business Logic ======

}
