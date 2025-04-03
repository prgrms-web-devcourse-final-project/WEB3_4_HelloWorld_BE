package org.helloworld.gymmate.domain.gym.machine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MachineCreateRequest(
	@NotBlank(message = "머신 이름은 필수 입력 값입니다.")
	String name,

	@NotNull(message = "머신 수는 필수 입력 값입니다.")
	@Min(value = 0, message = "머신 수는 0 이상이어야 합니다.")
	Integer amount
) {
}