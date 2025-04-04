package org.helloworld.gymmate.domain.gym.partnerGym.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GymRequest(
	@NotBlank(message = "운영 시작 시간을 입력해주세요.")
	String startTime,

	@NotBlank(message = "운영 종료 시간을 입력해주세요.")
	String endTime,

	@NotBlank(message = "헬스장 소개를 입력해주세요.")
	String intro
) {
}
