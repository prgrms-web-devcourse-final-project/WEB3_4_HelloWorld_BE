package org.helloworld.gymmate.domain.gym.partnerGym.dto.response;

public record GymProductResponse(
	Long gymProductId,
	Integer gymProductMonth,
	Integer gymProductFee
) {
}
