package org.helloworld.gymmate.domain.gym.gymticket.dto;

import java.time.LocalDate;

public record PartnerGymTicketResponse(
	long gymTicketId,
	String gymProductName,
	LocalDate startDate,
	LocalDate endDate,
	int gymProductFee,
	String status,
	String memberName
) {
}
