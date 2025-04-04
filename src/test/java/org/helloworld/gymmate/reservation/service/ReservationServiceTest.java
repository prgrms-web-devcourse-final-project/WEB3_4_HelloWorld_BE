package org.helloworld.gymmate.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.pt_product.service.PtProductService;
import org.helloworld.gymmate.domain.reservation.dto.ReservationRequest;
import org.helloworld.gymmate.domain.reservation.entity.Reservation;
import org.helloworld.gymmate.domain.reservation.repository.ReservationRepository;
import org.helloworld.gymmate.domain.reservation.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
	@InjectMocks
	private ReservationService reservationService;

	@Mock
	private PtProductService ptProductService;

	@Mock
	private ReservationRepository reservationRepository;

	@Test
	@DisplayName("예약 생성 성공")
	void registerSuccess() {
		// given
		Long userId = 1L;
		Long ptProductId = 1L;
		ReservationRequest request = new ReservationRequest(
			null,                   // reservationId
			"PT 상품",              // productName
			LocalDate.now().plusDays(1), // date (내일)
			14,                     // time (14시)
			100000L,               // price
			null,                  // cancelDate
			null                   // completedDate
		);

		PtProduct ptProduct = PtProduct.builder()
			.ptProductId(ptProductId)
			.ptProductName("PT 상품")
			.trainerId(2L)
			.ptProductFee(100000L)
			.build();

		Reservation savedReservation = Reservation.builder()
			.reservationId(1L)
			.productName(ptProduct.getPtProductName())
			.trainerId(ptProduct.getTrainerId())
			.date(request.date())
			.time(request.time())
			.price(ptProduct.getPtProductFee())
			.memberId(userId)
			.build();

		// when
		when(ptProductService.findProductOrThrow(ptProductId)).thenReturn(ptProduct);
		when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

		Long result = reservationService.register(userId, ptProductId, request);

		// then
		assertThat(result).isEqualTo(savedReservation.getReservationId());
		verify(ptProductService).findProductOrThrow(ptProductId);
		verify(reservationRepository).save(any(Reservation.class));
	}

	@Test
	@DisplayName("존재하지 않는 PT 상품으로 예약 시도 시 예외 발생")
	void registerWithNonExistentProduct() {
		// given
		Long userId = 1L;
		Long ptProductId = 999L;
		ReservationRequest request = new ReservationRequest(
			null, "PT 상품", LocalDate.now().plusDays(1), 14, 100000L, null, null
		);

		// when
		when(ptProductService.findProductOrThrow(ptProductId))
			.thenThrow(new EntityNotFoundException("존재하지 않는 PT 상품입니다."));

		// then
		assertThrows(EntityNotFoundException.class, () ->
			reservationService.register(userId, ptProductId, request)
		);
	}
}