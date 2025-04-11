package org.helloworld.gymmate.domain.gym.gymreview.repository;

import org.helloworld.gymmate.domain.gym.gymreview.entity.GymReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymReviewRepository extends JpaRepository<GymReview, Long> {
    void deleteAllByMemberId(Long memberId);
}
