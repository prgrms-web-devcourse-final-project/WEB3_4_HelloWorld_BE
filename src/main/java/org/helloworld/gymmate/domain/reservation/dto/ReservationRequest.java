package org.helloworld.gymmate.domain.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(
	Long reservationId,
	String productName,
	LocalDate date,
	LocalTime time,
	Long price,
	LocalDate cancelDate,
	LocalDate completedDate
) {
}
