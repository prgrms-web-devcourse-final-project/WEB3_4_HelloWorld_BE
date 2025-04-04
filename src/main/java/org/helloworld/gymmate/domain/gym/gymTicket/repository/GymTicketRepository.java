package org.helloworld.gymmate.domain.gym.gymTicket.repository;

import org.helloworld.gymmate.domain.gym.gymTicket.entity.GymTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymTicketRepository extends JpaRepository<GymTicket, Long> {
}
