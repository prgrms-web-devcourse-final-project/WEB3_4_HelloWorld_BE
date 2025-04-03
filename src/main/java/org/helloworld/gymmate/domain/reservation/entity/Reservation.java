package org.helloworld.gymmate.domain.reservation.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id", nullable = false)
	private Long reservationId;

	@Column(name = "product_name", nullable = false)
	private String productName;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private LocalTime time;

	@Column(nullable = false)
	private Integer price;

	@Column(name = "cancel_date")
	private LocalDate cancelDate;

	@Column(name = "completed_date")
	private LocalDate completedDate;
}
