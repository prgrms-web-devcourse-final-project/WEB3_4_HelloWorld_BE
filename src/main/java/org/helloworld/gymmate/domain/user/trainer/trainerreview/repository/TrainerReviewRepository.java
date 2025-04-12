package org.helloworld.gymmate.domain.user.trainer.trainerreview.repository;

import java.util.List;

import org.helloworld.gymmate.domain.user.trainer.trainerreview.entity.TrainerReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerReviewRepository extends JpaRepository<TrainerReview, Long> {
    List<TrainerReview> findAllByTrainer_TrainerId(Long trainerId);

    List<TrainerReview> findAllByMemberId(Long memberId);
}
