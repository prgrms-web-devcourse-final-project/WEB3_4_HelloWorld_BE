package org.helloworld.gymmate.domain.gym.gymreview.repository;

import org.helloworld.gymmate.domain.gym.gymreview.entity.GymReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GymReviewRepository extends JpaRepository<GymReview, Long> {
    void deleteAllByMemberId(Long memberId);

    @Query("SELECT gr FROM GymReview gr JOIN gr.partnerGym pg JOIN pg.gym g WHERE g.gymId = :gymId")
    Page<GymReview> findAll(long gymId, Pageable pageable);

    @Query("SELECT AVG(gr.score) FROM GymReview gr WHERE gr.partnerGym.id = :partnerGymId")
    Double findAverageScoreByPartnerGymId(@Param("partnerGymId") Long partnerGymId);
}
