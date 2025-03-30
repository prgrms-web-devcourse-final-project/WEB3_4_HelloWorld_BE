package org.helloworld.gymmate.domain.user.member.entity;

import org.helloworld.gymmate.domain.user.enumerate.GenderType;
import org.helloworld.gymmate.domain.user.model.SocialProvider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "member")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long memberId;

	@Column(name = "phone_number",nullable = false)
	private String phoneNumber;

	@Column(name = "member_name",nullable = false)
	private String memberName;

	@Column(name="email",nullable = false)
	private String email;

	@Column(name="birthday",nullable = false)
	private String birthday;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "gender",nullable = false)
	private GenderType genderType;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "social_provider_id")
	private SocialProvider socialProvider;
}
