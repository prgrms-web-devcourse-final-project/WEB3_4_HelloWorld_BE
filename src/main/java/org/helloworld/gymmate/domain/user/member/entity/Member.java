package org.helloworld.gymmate.domain.user.member.entity;

import org.helloworld.gymmate.domain.user.enumerate.GenderType;

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
@Table(name = "member")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long memberId;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "member_name")
	private String memberName;

	@Column(name="email")
	private String email;

	@Column(name="birthday")
	private String birthday;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "gender")
	private GenderType genderType;

}
