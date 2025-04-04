package org.helloworld.gymmate.domain.gym.gymInfo.dto.request;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductRequest;

public record UpdateGymRequest(
	Long partnerGymId,
	GymInfoRequest gymInfoRequest,
	List<GymProductRequest> gymProductRequest,
	List<Long> deleteImageIds
) {
}
