package org.helloworld.gymmate.domain.user.trainer.trainerreview.entity;

import java.util.ArrayList;
import java.util.List;

import org.helloworld.gymmate.common.entity.BaseEntity;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.hibernate.annotations.BatchSize;

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
@Table(name = "trainer_review")
public class TrainerReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_review_id")
    private Long trainerReviewId;

    private Double score;

    private String content;

    @Column(name = "member_id")
    private Long memberId;

    @OneToMany(mappedBy = "trainerReview", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @Builder.Default
    private List<TrainerReviewImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    public void addImages(List<TrainerReviewImage> images) {
        this.images.addAll(images);
        images.forEach(img -> img.assignTrainerReview(this));
    }

    public void removeImage(TrainerReviewImage image) {
        images.remove(image);
        image.assignTrainerReview(null);
    }
}
