package org.helloworld.gymmate.domain.user.trainer.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    List<Trainer> findByTrainerIdIn(Set<Long> trainerIds);

    Optional<Trainer> findByOauth(Oauth oauth);

    Optional<Trainer> findByTrainerId(Long trainerId);

    Page<Trainer> findAllByOrderByTrainerIdDesc(Pageable pageable);

    Page<Trainer> findByTrainerNameContainingIgnoreCaseOrderByTrainerIdDesc(String searchTerm, Pageable pageable);

    @Query("SELECT t FROM Trainer t JOIN t.gym g WHERE g.address LIKE CONCAT('%', :searchTerm, '%') ORDER BY t.trainerId DESC")
    Page<Trainer> findByGymAddressOrderByTrainerIdDesc(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<Trainer> findAllByOrderByScoreDesc(Pageable pageable);

    Page<Trainer> findByTrainerNameContainingIgnoreCaseOrderByScoreDesc(String searchTerm, Pageable pageable);

    @Query("SELECT t FROM Trainer t JOIN t.gym g WHERE LOWER(g.address) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY t.score DESC")
    Page<Trainer> findByGymAddressOrderByScoreDesc(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query(value = """
        SELECT t.*
        FROM gymmate_trainer t
        JOIN gym g ON t.gym_id = g.gym_id
        WHERE (:searchOption = 'NONE' OR
               (:searchOption = 'TRAINER' AND t.trainer_name LIKE CONCAT('%', :searchTerm, '%')) OR
               (:searchOption = 'DISTRICT' AND g.address LIKE CONCAT('%', :searchTerm, '%')))
          AND ST_Within(
              g.location,
              ST_GeomFromText(:boundingBox, 4326)
          )
        ORDER BY ST_Distance_Sphere(
            ST_GeomFromText(CONCAT('POINT(', :y, ' ', :x, ')'), 4326),
            g.location
        ) ASC
        """,
        countQuery = """
            SELECT COUNT(*)
            FROM gymmate_trainer t
            JOIN gym g ON t.gym_id = g.gym_id
            WHERE (:searchOption = 'NONE' OR
                   (:searchOption = 'TRAINER' AND t.trainer_name LIKE CONCAT('%', :searchTerm, '%')) OR
                   (:searchOption = 'DISTRICT' AND g.address LIKE CONCAT('%', :searchTerm, '%')))
              AND ST_Within(
                  g.location,
                  ST_GeomFromText(:boundingBox, 4326)
              )
            """,
        nativeQuery = true)
    Page<Trainer> findNearbyTrainersWithSearch(
        @Param("x") Double x, @Param("y") Double y,
        @Param("boundingBox") String boundingBoxWkt,
        @Param("searchOption") String searchOption, @Param("searchTerm") String searchTerm,
        Pageable pageable);

    List<Trainer> findByGym_GymId(Long gymId);

    List<Trainer> findByIsOwnerTrue();
}