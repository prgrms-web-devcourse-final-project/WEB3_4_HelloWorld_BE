package org.helloworld.gymmate.domain.gym.partnerGym.dto.request;

import java.util.List;

import org.helloworld.gymmate.domain.gym.facility.dto.FacilityRequest;
import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductRequest;

import jakarta.validation.Valid;

public record GymInfoRequest(
	@Valid GymRequest gymRequest,
	@Valid FacilityRequest facilityRequest,
	@Valid List<GymProductRequest> gymProductRequest,
	List<Long> gymProductDeleteIds // 생략 가능
) {
}
