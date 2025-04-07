package org.helloworld.gymmate.domain.gym.partnergym.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

public record GymRegisterRequest(
	@Positive Long gymId,
	@Valid GymInfoRequest gymInfoRequest
) {
}