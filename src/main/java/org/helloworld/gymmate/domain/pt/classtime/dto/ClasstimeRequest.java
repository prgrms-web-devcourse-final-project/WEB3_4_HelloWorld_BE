package org.helloworld.gymmate.domain.pt.classtime.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ClasstimeRequest(
	@NotNull(message = "요일은 필수 입력 값입니다.")
	@Min(value = 0, message = "요일 값은 0(일요일)부터 6(토요일) 사이여야 합니다.")
	@Max(value = 6, message = "요일 값은 0(일요일)부터 6(토요일) 사이여야 합니다.")
	Integer dayOfWeek,

	@NotNull(message = "시간은 필수 입력 값입니다.")
	@Min(value = 0, message = "시간 값은 0부터 23 사이여야 합니다.")
	@Max(value = 23, message = "시간 값은 0부터 23 사이여야 합니다.")
	Integer time
) {}