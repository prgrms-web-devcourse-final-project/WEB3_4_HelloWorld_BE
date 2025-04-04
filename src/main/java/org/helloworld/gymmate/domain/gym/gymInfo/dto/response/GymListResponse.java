package org.helloworld.gymmate.domain.gym.gymInfo.dto.response;

public record GymListResponse(
	Long gymId,
	String gymName,
	String startTime,
	String endTime,
	String placeUrl,
	Double avgScore,
	Boolean isPartner,
	String imageUrl
) {
}
