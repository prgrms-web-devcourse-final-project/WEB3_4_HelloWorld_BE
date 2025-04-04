package org.helloworld.gymmate.domain.gym.partnerGym.dto.request;

import java.util.List;

import org.helloworld.gymmate.domain.gym.facility.dto.FacilityRequest;
import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductDeleteRequest;
import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductRequest;

public record GymInfoRequest(
	GymRequest gymRequest,
	FacilityRequest facilityRequest,
	List<GymProductRequest> gymProductRequest,
	List<GymProductDeleteRequest> deleteGymProductRequest // 생략 가능
) {
}
