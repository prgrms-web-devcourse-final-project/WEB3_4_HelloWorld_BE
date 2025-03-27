package org.helloworld.gymmate.domain.user.model;

import org.helloworld.gymmate.domain.user.enumerate.GenderType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "gymmate_user")
public class GymmateUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long userId;

	@Column(name = "member_name")
	private String userName;

	@Column(name = "phone_number", unique = true)
	private String phoneNumber;

	@Column(name = "birthday")
	private String birthday;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "gender")
	private GenderType genderType;

}
