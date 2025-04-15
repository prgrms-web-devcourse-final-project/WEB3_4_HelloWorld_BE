package org.helloworld.gymmate.domain.user.trainer.trainerreview.repository;

import java.util.List;

import org.helloworld.gymmate.domain.user.trainer.trainerreview.entity.TrainerReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrainerReviewRepository extends JpaRepository<TrainerReview, Long> {
    List<TrainerReview> findAllByTrainer_TrainerId(Long trainerId);

    List<TrainerReview> findAllByMemberId(Long memberId);

    Page<TrainerReview> findAllByTrainer_TrainerId(Pageable pageable, Long trainerId);

    @Query("SELECT AVG(tr.score) FROM TrainerReview tr WHERE tr.trainer.id = :trainerId")
    Double findAverageScoreByTrainerId(@Param("trainerId") Long trainerId);
}
