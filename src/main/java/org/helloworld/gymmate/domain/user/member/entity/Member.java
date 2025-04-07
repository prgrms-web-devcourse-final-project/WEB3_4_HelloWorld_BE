package org.helloworld.gymmate.domain.user.member.entity;

import java.util.ArrayList;
import java.util.List;

import org.helloworld.gymmate.domain.gym.gymticket.entity.GymTicket;
import org.helloworld.gymmate.domain.user.converter.GenderTypeConverter;
import org.helloworld.gymmate.domain.user.enums.GenderType;
import org.helloworld.gymmate.domain.user.member.dto.MemberModifyRequest;
import org.helloworld.gymmate.domain.user.member.dto.MemberRequest;
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
@Table(name = "gymmate_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "email")
    private String email;

    @Column(name = "birthday")
    private String birthday;

    @Convert(converter = GenderTypeConverter.class)
    @Column(name = "gender_type")
    private GenderType genderType;

    @Column(name = "height")
    private String height; //신체정보

    @Column(name = "weight")
    private String weight;

    @Column(name = "address")
    private String address; //주소

    @Column(name = "x_field")
    private Double xField;

    @Column(name = "y_field")
    private Double yField;

    @Column(name = "recent_bench")
    private Double recentBench; //3대 - 밴치프레스

    @Column(name = "recent_deadlift")
    private Double recentDeadlift; //3대 - 데드리프트

    @Column(name = "recent_squat")
    private Double recentSquat; //3대 - 스쿼트

    @Column(name = "level")
    private Integer level; //3대 - 레벨

    @Column(name = "is_account_nonlocked")
    private Boolean isAccountNonLocked; //계정잠김여부

    @Column(name = "additional_info_completed")
    private Boolean additionalInfoCompleted; // 추가 정보 입력 여부

    @Column(name = "cash")
    private Long cash;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "oauth_id")
    private Oauth oauth;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GymTicket> gymTickets = new ArrayList<>();

    // ====== Business Logic ======

    public void registerMemberInfo(MemberRequest request) {
        this.phoneNumber = request.phoneNumber();
        this.memberName = request.memberName();
        this.email = request.email();
        this.birthday = request.birthday();
        this.genderType = GenderType.fromString(request.gender());
        this.height = request.height();
        this.weight = request.weight();
        this.address = request.address();
        this.xField = request.xField();
        this.yField = request.yField();
        this.recentBench = request.recentBench();
        this.recentDeadlift = request.recentDeadlift();
        this.recentSquat = request.recentSquat();
        this.additionalInfoCompleted = true;

    }

    public void modifyMemberInfo(MemberModifyRequest request) {
        this.phoneNumber = request.phoneNumber();
        this.memberName = request.memberName();
        this.email = request.email();
        this.birthday = request.birthday();
        this.genderType = GenderType.fromString(request.gender());
        this.height = request.height();
        this.weight = request.weight();
        this.address = request.address();
        this.xField = request.xField();
        this.yField = request.yField();
        this.recentBench = request.recentBench();
        this.recentDeadlift = request.recentDeadlift();
        this.recentSquat = request.recentSquat();
        this.additionalInfoCompleted = true;
    }

    public void updateCash(Long updateCash) {
        this.cash = updateCash;
    }
}
