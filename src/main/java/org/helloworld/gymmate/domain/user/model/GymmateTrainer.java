package org.helloworld.gymmate.domain.user.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.helloworld.gymmate.domain.user.dto.OwnerRegisterRequest;
import org.helloworld.gymmate.domain.user.dto.TrainerRegisterRequest;
import org.helloworld.gymmate.domain.user.enumerate.GenderType;

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
@Table(name = "gymmate_trainer")
public class GymmateTrainer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long trainerId;

	@Column(name = "traine_name")
	private String trainerName;

	@Column(name = "phone_number", unique = true)
	private String phoneNumber;

	private String email;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "gender")
	private GenderType genderType;

	private String bank;

	private String account;

	@Column(name = "business_number", unique = true)
	private String businessNumber;

	private Boolean isOwner;

	private Boolean isCheck;

	private String profile;

	private Boolean isAccountNonLocked;

	private Long cash;

	private Double score; // 리뷰 평점 평균

	private Long gymId;

	private Boolean additionalInfoCompleted; // 추가 정보 입력 여부

	private LocalDate date;
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "social_provider_id")
	private SocialProvider socialProvider;

	public void updateTrainerInfo(TrainerRegisterRequest request) {
		this.trainerName = request.getTrainerName();
		this.phoneNumber = request.getPhoneNumber();
		this.email = request.getEmail();
		this.genderType = GenderType.fromString(request.getGender());
		this.bank = request.getBank();
		this.account = request.getAccount();
		this.additionalInfoCompleted = true;
	}

	public void updateOwnerInfo(OwnerRegisterRequest request) {
		this.trainerName = request.getTrainerName();
		this.phoneNumber = request.getPhoneNumber();
		this.email = request.getEmail();
		this.genderType = GenderType.fromString(request.getGender());
		this.bank = request.getBank();
		this.account = request.getAccount();
		this.businessNumber = request.getBusinessNumber();
		this.date = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE);
		this.additionalInfoCompleted = true;
	}
}
