package org.helloworld.gymmate.domain.user.trainer.trainerreview.repository;

import org.helloworld.gymmate.domain.user.trainer.trainerreview.entity.TrainerReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerReviewRepository extends JpaRepository<TrainerReview, Long> {
}
