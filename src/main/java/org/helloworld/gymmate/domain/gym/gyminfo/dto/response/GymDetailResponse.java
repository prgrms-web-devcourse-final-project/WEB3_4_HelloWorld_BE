package org.helloworld.gymmate.domain.gym.gyminfo.dto.response;

public record GymDetailResponse(
	Long gymId,
	String gymName,
	String startTime,
	String endTime,
	String phoneNumber,
	String address,
	String xField,
	String yField,
	Double avgScore,
	String intro,
	Boolean isPartner
) {
}
