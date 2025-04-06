package org.helloworld.gymmate.domain.gym.gymticket.repository;

import org.helloworld.gymmate.domain.gym.gymticket.entity.GymTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymTicketRepository extends JpaRepository<GymTicket, Long> {
}
