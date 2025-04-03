package org.helloworld.gymmate.domain.gym.gymInfo.dto.request;

import java.util.List;

public record RegisterGymRequest(
	Long gymId,
	GymInfoRequest gymInfoRequest,
	List<GymProductRequest> gymProductRequest,
	List<String> imageUrl
) {
}