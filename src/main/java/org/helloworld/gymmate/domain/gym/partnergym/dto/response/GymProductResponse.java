package org.helloworld.gymmate.domain.gym.partnergym.dto.response;

public record GymProductResponse(
	Long gymProductId,
	Integer gymProductMonth,
	Integer gymProductFee
) {
}
