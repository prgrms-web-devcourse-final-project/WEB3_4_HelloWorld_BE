package org.helloworld.gymmate.domain.pt.reservation.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
	Long reservationId,

	@NotNull(message = "요일은 필수 입력 값입니다.")
	@Min(value = 0, message = "요일 값은 0(일요일)부터 6(토요일) 사이여야 합니다.")
	@Max(value = 6, message = "요일 값은 0(일요일)부터 6(토요일) 사이여야 합니다.")
	Integer dayOfWeek,

	@NotNull(message = "시간은 필수 입력 값입니다.")
	@Min(value = 0, message = "시간 값은 0부터 23 사이여야 합니다.")
	@Max(value = 23, message = "시간 값은 0부터 23 사이여야 합니다.")
	Integer time,

	LocalDate cancelDate, // 예약 생성 시에는 필요 없음

	LocalDate completedDate // 예약 생성 시에는 필요 없음
) {
	// TODO: 추가 유효성 검사를 위한 생성자:
	// 1. 예약날짜와 시간이 현재보다 이후인지.
	// 2. cancelDate가 예약날짜 이후인지
}
