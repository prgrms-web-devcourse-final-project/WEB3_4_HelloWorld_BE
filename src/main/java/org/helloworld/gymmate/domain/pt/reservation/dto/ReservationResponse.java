package org.helloworld.gymmate.domain.pt.reservation.dto;

import java.time.LocalDate;

public record ReservationResponse(
	Long reservationId,
	String productName,
	LocalDate date,
	Integer time,
	Long price,
	LocalDate cancelDate,
	LocalDate completedDate,
	Long trainerId
) {
}