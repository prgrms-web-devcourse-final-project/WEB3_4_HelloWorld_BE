package org.helloworld.gymmate.domain.pt.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.domain.pt.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByMemberId(Long memberId, Pageable pageable);

    Page<Reservation> findByTrainerId(Long trainerId, Pageable pageable);

    List<Reservation> findByTrainerId(Long trainerId);

    Optional<Reservation> findByMemberIdAndTrainerId(Long memberId, Long trainerId);
}
