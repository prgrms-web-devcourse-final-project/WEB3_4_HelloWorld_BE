package org.helloworld.gymmate.domain.pt.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.domain.pt.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByMemberId(Long memberId, Pageable pageable);

    Page<Reservation> findByTrainerId(Long trainerId, Pageable pageable);

    List<Reservation> findByTrainerId(Long trainerId);

    @Query(value = """
        SELECT r.* FROM reservation r
        WHERE r.trainer_id = :trainerId
          AND r.member_id = :memberId
          AND r.cancel_date IS NULL
          AND DATE_ADD(r.date, INTERVAL r.time HOUR) <= CURRENT_TIMESTAMP
        LIMIT 1
        """, nativeQuery = true)
    Optional<Reservation> find(@Param("memberId") Long memberId, @Param("trainerId") Long trainerId);

    boolean existsByMemberIdAndCancelDateIsNullAndCompletedDateIsNull(Long memberId);

    @Modifying
    @Query("UPDATE Reservation r SET r.memberId = null WHERE r.memberId = :memberId")
    void setMemberIdNullByMemberId(@Param("memberId") Long memberId);

    void deleteAllByTrainerId(Long trainerId);
}
