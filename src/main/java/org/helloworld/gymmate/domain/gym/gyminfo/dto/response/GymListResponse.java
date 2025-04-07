package org.helloworld.gymmate.domain.gym.gyminfo.dto.response;

public record GymListResponse(
	Long gymId,
	String gymName,
	String startTime,
	String endTime,
	String placeUrl,
	Double avgScore,
	Boolean isPartner,
	String thumbnailImage
) {
}
