package org.helloworld.gymmate.domain.gym.partnerGym.dto.request;

import java.util.List;

import jakarta.validation.Valid;

public record GymUpdateRequest(
	@Valid GymInfoRequest gymInfoRequest,
	List<Long> deleteImageIds
) {
}
