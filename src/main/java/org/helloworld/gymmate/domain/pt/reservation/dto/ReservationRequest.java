package org.helloworld.gymmate.domain.pt.reservation.dto;

import java.time.LocalDate;

public record ReservationRequest(
    LocalDate date,

    Integer time,

    LocalDate cancelDate, // 예약 생성 시에는 필요 없음

    LocalDate completedDate // 예약 생성 시에는 필요 없음
) {
    // TODO: 추가 유효성 검사를 위한 생성자:
    // 1. 예약날짜와 시간이 현재보다 이후인지.
    // 2. cancelDate가 예약날짜 이후인지
}
