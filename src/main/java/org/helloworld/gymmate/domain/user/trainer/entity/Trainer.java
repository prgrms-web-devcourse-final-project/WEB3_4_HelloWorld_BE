package org.helloworld.gymmate.domain.user.trainer.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.helloworld.gymmate.domain.gym.gyminfo.entity.Gym;
import org.helloworld.gymmate.domain.pt.student.entity.Student;
import org.helloworld.gymmate.domain.user.converter.GenderTypeConverter;
import org.helloworld.gymmate.domain.user.enums.GenderType;
import org.helloworld.gymmate.domain.user.trainer.dto.OwnerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerModifyRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerProfileRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerRegisterRequest;
import org.helloworld.gymmate.security.oauth.entity.Oauth;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_id")
    private Long trainerId;

    @Column(name = "trainer_name")
    private String trainerName;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Convert(converter = GenderTypeConverter.class)
    @Column(name = "gender_type")
    private GenderType genderType;

    @Column(name = "bank")
    private String bank;

    @Column(name = "account")
    private String account;

    @Column(name = "business_number", unique = true)
    private String businessNumber;

    @Column(name = "business_date")
    private LocalDate businessDate;

    @Column(name = "is_owner")
    private Boolean isOwner;

    @Column(name = "is_check")
    private Boolean isCheck;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "is_account_non_locked")
    private Boolean isAccountNonLocked;

    @Column(name = "cash")
    private Long cash;

    @Column(name = "score")
    private Double score; // 리뷰 평점 평균

    @Column(name = "intro")
    private String intro; // 소개글

    @Column(name = "career")
    private String career;

    @Column(name = "field")
    private String field;

    @Column(name = "additional_info_completed")
    private Boolean additionalInfoCompleted; // 추가 정보 입력 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "oauth_id")
    private Oauth oauth;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Student> students = new ArrayList<>();

    // ====== Business Logic ======

    public void registerTrainerInfo(TrainerRegisterRequest request) { // 나중에 헬스장 추가해야함
        this.trainerName = request.trainerName();
        this.phoneNumber = request.phoneNumber();
        this.email = request.email();
        this.genderType = GenderType.fromString(request.gender());
        this.bank = request.bank();
        this.account = request.account();
        this.additionalInfoCompleted = true;
    }

    public void assignGym(Gym gym) {
        this.gym = gym;
    }

    public void registerOwnerInfo(OwnerRegisterRequest request) {
        this.isOwner = true;
        this.isCheck = true;
        this.trainerName = request.trainerName();
        this.phoneNumber = request.phoneNumber();
        this.email = request.email();
        this.genderType = GenderType.fromString(request.gender());
        this.bank = request.bank();
        this.account = request.account();
        this.businessNumber = request.businessNumber();
        this.businessDate = LocalDate.parse(request.businessDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.additionalInfoCompleted = true;
    }

    public void modifyTrainerInfo(TrainerModifyRequest request, String imageUrl) {
        this.trainerName = request.trainerName();
        this.phoneNumber = request.phoneNumber();
        this.email = request.email();
        this.bank = request.bank();
        this.account = request.account();
        this.intro = request.intro();
        this.profileUrl = imageUrl;
        this.career = request.career();
        this.field = request.field();
    }

    public void updateTrainerProfile(TrainerProfileRequest request) {
        this.intro = request.intro();
        this.career = request.career();
        this.field = request.field();
    }

    public void updateGym(Gym gym) {
        this.gym = gym;
    }

    public void updateScore(double score) {
        this.score = score;
    }
}
