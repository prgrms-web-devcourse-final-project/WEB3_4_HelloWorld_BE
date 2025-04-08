package org.helloworld.gymmate.domain.pt.reservation.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ReservationByMonthResponse(
	Map<LocalDate, List<Integer>> reservationTimes //날짜와 시간 목록
) {
}