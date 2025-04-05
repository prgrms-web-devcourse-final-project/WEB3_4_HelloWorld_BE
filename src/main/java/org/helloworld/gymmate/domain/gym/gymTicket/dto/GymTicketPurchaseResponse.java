package org.helloworld.gymmate.domain.gym.gymTicket.dto;

public record GymTicketPurchaseResponse(
	long memberCash,
	long gymProductFee,
	long remainMemberCash,
	boolean available
) {
}
