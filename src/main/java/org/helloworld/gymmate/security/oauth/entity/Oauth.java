package org.helloworld.gymmate.security.oauth.entity;

import java.time.LocalDate;

import org.helloworld.gymmate.common.entity.BaseEntity;
import org.helloworld.gymmate.domain.user.enums.SocialProviderType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "provider", nullable = false)
	private SocialProviderType provider;

	@Column(name = "provider_id", nullable = false)
	private String providerId;
}
