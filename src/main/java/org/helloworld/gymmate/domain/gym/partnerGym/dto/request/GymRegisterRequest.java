package org.helloworld.gymmate.domain.gym.partnerGym.dto.request;

public record GymRegisterRequest(
	Long gymId,
	GymInfoRequest gymInfoRequest
) {
}