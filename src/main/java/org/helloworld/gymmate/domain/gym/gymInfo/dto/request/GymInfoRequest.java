package org.helloworld.gymmate.domain.gym.gymInfo.dto.request;

import org.helloworld.gymmate.domain.gym.facility.dto.FacilityRequest;

public record GymInfoRequest(
	GymRequest gymRequest,
	FacilityRequest facilityRequest
) {
}
