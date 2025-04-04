package org.helloworld.gymmate.domain.reservation.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservationRequest(
	Long reservationId,

	@NotBlank(message = "상품명은 필수입니다")
	String productName,

	@NotNull(message = "예약 날짜는 필수입니다")
	LocalDate date,

	@NotNull(message = "예약 시간은 필수입니다")
	@Min(value = 0, message = "예약 시간은 0시 이상이어야 합니다")
	@Max(value = 23, message = "예약 시간은 23시 이하여야 합니다")
	Integer time,

	@NotNull(message = "가격은 필수입니다")
	@Positive(message = "가격은 0보다 커야 합니다")
	Long price,

	LocalDate cancelDate, // 예약 생성 시에는 필요 없음

	LocalDate completedDate // 예약 생성 시에는 필요 없음
) {
	// TODO: 추가 유효성 검사를 위한 생성자:
	// 1. 예약날짜와 시간이 현재보다 이후인지.
	// 2. cancelDate가 예약날짜 이후인지
}
