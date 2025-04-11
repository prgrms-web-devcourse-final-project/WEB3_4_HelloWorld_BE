package org.helloworld.gymmate.domain.user.trainer.award.repository;

import java.util.List;

import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardRepository extends JpaRepository<Award, Long> {
    List<Award> findByTrainerId(Long trainerId);

    List<Award> findByTrainerIdIn(List<Long> trainerIds);

    void deleteAllByTrainerId(Long trainerId);
}