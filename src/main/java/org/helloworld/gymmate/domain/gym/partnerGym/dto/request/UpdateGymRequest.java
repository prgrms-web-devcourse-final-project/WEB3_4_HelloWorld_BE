package org.helloworld.gymmate.domain.gym.partnerGym.dto.request;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductRequest;

public record UpdateGymRequest(
	GymInfoRequest gymInfoRequest,
	List<GymProductRequest> gymProductRequest,
	//TODO: List<GymProductRequest> deleteGymProductRequest
	List<Long> deleteImageIds
) {
}
