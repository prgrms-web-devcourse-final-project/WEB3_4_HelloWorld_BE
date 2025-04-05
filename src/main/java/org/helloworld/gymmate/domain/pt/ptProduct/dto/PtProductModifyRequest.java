package org.helloworld.gymmate.domain.pt.ptProduct.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PtProductModifyRequest(
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	String ptProductName,

	@NotBlank(message = "정보는 필수 입력 값입니다.")
	String info,

	@NotNull(message = "PT 수업 요금은 필수 입력 값입니다.")
	@Min(value = 0, message = "PT 수업 요금은 0원 이상이어야 합니다.")
	Long ptProductFee,

	List<String> deleteImageIds
) {
}
