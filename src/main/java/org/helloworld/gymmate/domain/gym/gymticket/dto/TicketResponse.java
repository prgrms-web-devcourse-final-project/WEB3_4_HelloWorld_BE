package org.helloworld.gymmate.domain.gym.gymticket.dto;

import java.time.LocalDate;

public record TicketResponse(
	long gymTicketId,
	String gymProductName,
	LocalDate startDate,
	LocalDate endDate,
	int gymProductFee,
	String status,
	long partnerGymId
) {
}