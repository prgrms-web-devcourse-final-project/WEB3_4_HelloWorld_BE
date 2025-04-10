package org.helloworld.gymmate.domain.gym.gymreview.entity;

import java.util.ArrayList;
import java.util.List;

import org.helloworld.gymmate.common.entity.BaseEntity;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "gym_review")
public class GymReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_review_id")
    private Long gymReviewId;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "content")
    private String content;

    // 이미지 연결 OneToMany
    @OneToMany(mappedBy = "gymReview", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GymReviewImage> images = new ArrayList<>();

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    // 파트너짐id ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_gym_id")
    private PartnerGym partnerGym;

    // ====== Business Logic ======

}
