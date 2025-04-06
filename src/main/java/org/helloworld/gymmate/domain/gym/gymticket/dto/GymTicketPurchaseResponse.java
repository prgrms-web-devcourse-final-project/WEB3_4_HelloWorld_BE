package org.helloworld.gymmate.domain.gym.gymticket.dto;

public record GymTicketPurchaseResponse(
	long memberCash,
	long gymProductFee,
	long remainMemberCash,
	boolean available
) {
}
