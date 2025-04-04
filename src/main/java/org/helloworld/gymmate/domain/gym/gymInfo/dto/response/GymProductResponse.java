package org.helloworld.gymmate.domain.gym.gymInfo.dto.response;

public record GymProductResponse(
	Long gymProductId,
	Integer gymProductMonth,
	Integer gymProductFee
) {
}
