package org.helloworld.gymmate.domain.gym.partnergym.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GymRequest(
	@NotBlank String startTime,
	@NotBlank String endTime,
	@NotBlank String intro
) {
}
