package org.helloworld.gymmate.domain.gym.partnerGym.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

public record GymRegisterRequest(
	@Positive(message = "헬스장 Id를 넣어주세요.")
	Long gymId,
	@Valid GymInfoRequest gymInfoRequest
) {
}