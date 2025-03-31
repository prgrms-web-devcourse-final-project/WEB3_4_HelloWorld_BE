package org.helloworld.gymmate.domain.user.member.entity;

import org.helloworld.gymmate.domain.user.enums.GenderType;

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

	//신체정보
	@Column(name="height")
	private String height;

	@Column(name="weight")
	private String weight;


	//주소
	@Column(name="address")
	private String address;

	@Column(name="x_field")
	private String xField;

	@Column(name="y_field")
	private String yField;

	//3대
	@Column(name="recent_bench")
	private Double recentBench;

	@Column(name="recent_deadlift")
	private Double recentDeadlift;

	@Column(name="recent_squat")
	private Double recentSquat;

	//계정잠김여부
	@Column(name="is_account_nonloked")
	private Boolean isAccountNonLocked;

	@Column(name="cash")
	private Long cash;
}
