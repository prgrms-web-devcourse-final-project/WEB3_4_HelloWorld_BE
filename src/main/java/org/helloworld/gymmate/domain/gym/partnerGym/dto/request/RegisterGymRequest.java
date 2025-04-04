package org.helloworld.gymmate.domain.gym.partnerGym.dto.request;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductRequest;

public record RegisterGymRequest(
	Long gymId,
	GymInfoRequest gymInfoRequest,
	List<GymProductRequest> gymProductRequest
) {
}