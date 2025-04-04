package org.helloworld.gymmate.domain.reservation.repository;

import org.helloworld.gymmate.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	Page<Reservation> findByMemberId(Long memberId, Pageable pageable);
}
