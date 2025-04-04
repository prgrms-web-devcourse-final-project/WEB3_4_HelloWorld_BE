package org.helloworld.gymmate.domain.gym.gymInfo.dto.response;

import org.helloworld.gymmate.domain.gym.facility.dto.FacilityResponse;

public record GymInfoResponse(
	String startTime,
	String endTime,
	String intro,
	FacilityResponse facility
) {
}
