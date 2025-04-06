package org.helloworld.gymmate.domain.gym.gymProduct.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GymProductRequest(
	@Nullable
	Long gymProductId, //비워서 보내면 새로 등록, 값이 있으면 수정

	@NotBlank(message = "헬스장 이용권 이름을 작성해주세요.")
	String gymProductName,

	@NotNull(message = "이용 금액을 선택해주세요.")
	Integer gymProductFee,

	@NotNull(message = "헬스장 이용 개월수를 선택해주세요.")
	Integer gymProductMonth
) {
}
