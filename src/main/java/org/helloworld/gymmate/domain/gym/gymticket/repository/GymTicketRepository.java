package org.helloworld.gymmate.domain.gym.gymticket.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.domain.gym.gymticket.entity.GymTicket;
import org.helloworld.gymmate.domain.gym.gymticket.enums.GymTicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymTicketRepository extends JpaRepository<GymTicket, Long> {
    List<GymTicket> findByEndDateBeforeAndStatus(LocalDate today, GymTicketStatus gymTicketStatus);

    Page<GymTicket> findByMember_MemberId(Long memberId, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<GymTicket> findAllByPartnerGymId(Long partnerGymId, Pageable pageable);

    Optional<GymTicket> findByPartnerGymIdAndMember_MemberId(Long partnerGymId, Long memberId);

    boolean existsByMember_MemberIdAndStatus(Long memberId, GymTicketStatus gymTicketStatus);
}
