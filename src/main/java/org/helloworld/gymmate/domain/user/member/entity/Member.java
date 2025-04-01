package org.helloworld.gymmate.domain.user.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.helloworld.gymmate.domain.user.enums.GenderType;
import org.helloworld.gymmate.domain.user.member.dto.MemberRequest;
import org.helloworld.gymmate.security.oauth.entity.Oauth;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "member")
@EqualsAndHashCode(of = "memberId")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "birthday", nullable = false)
    private String birthday;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "genderType", nullable = false)
    private GenderType genderType;

    //신체정보
    @Column(name = "height")
    private String height;

    @Column(name = "weight")
    private String weight;

    //주소
    @Column(name = "address")
    private String address;

    @Column(name = "x_field")
    private String xField;

    @Column(name = "y_field")
    private String yField;

    //3대
    @Column(name = "recent_bench")
    private Double recentBench;

    @Column(name = "recent_deadlift")
    private Double recentDeadlift;

    @Column(name = "recent_squat")
    private Double recentSquat;

    @Column(name = "level")
    private Integer level;

    //계정잠김여부
    @Column(name = "is_account_nonloked")
    private Boolean isAccountNonLocked;

    @Column(name = "cash")
    private Long cash;

    private Boolean additionalInfoCompleted; // 추가 정보 입력 여부

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "oauth_id")
    private Oauth oauth;

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
}
