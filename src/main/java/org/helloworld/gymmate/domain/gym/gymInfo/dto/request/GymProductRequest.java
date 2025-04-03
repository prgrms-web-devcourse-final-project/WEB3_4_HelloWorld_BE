package org.helloworld.gymmate.domain.gym.gymInfo.dto.request;

import jakarta.validation.constraints.NotNull;

public record GymProductRequest(
	@NotNull(message = "헬스장 이용 개월수를 선택해주세요.")
	Integer gymProductMonth,

	@NotNull(message = "이용 금액을 선택해주세요.")
	Integer gymProductFee
) {
}
