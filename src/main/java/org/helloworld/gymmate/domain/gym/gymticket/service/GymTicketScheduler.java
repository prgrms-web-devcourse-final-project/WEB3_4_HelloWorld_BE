package org.helloworld.gymmate.domain.gym.gymticket.service;

import java.time.LocalDate;
import java.util.List;

import org.helloworld.gymmate.domain.gym.gymticket.entity.GymTicket;
import org.helloworld.gymmate.domain.gym.gymticket.enums.GymTicketStatus;
import org.helloworld.gymmate.domain.gym.gymticket.repository.GymTicketRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymTicketScheduler {

	private final GymTicketRepository gymTicketRepository;

	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void expireGymTickets() {
		LocalDate today = LocalDate.now();
		List<GymTicket> expiredTickets = gymTicketRepository
			.findByEndDateBeforeAndStatus(today, GymTicketStatus.ACTIVE);

		for (GymTicket ticket : expiredTickets) {
			ticket.updateStatus(GymTicketStatus.EXPIRED);
		}
	}
}