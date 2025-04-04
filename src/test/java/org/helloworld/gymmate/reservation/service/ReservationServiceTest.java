package org.helloworld.gymmate.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.pt_product.service.PtProductService;
import org.helloworld.gymmate.domain.reservation.dto.ReservationRequest;
import org.helloworld.gymmate.domain.reservation.dto.ReservationResponse;
import org.helloworld.gymmate.domain.reservation.entity.Reservation;
import org.helloworld.gymmate.domain.reservation.repository.ReservationRepository;
import org.helloworld.gymmate.domain.reservation.service.ReservationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
	@InjectMocks
	private ReservationService reservationService;

	@Mock
	private PtProductService ptProductService;

	@Mock
	private ReservationRepository reservationRepository;

	private Long memberId;
	private int page;
	private int pageSize;
	private Sort sort;
	private List<Reservation> reservations;
	private PageRequest pageRequest;

	@BeforeEach
	void setUp() {
		// 공통으로 사용할 테스트 데이터 설정
		memberId = 1L;
		page = 0;
		pageSize = 10;
		sort = Sort.by(Sort.Direction.DESC, "date");
		pageRequest = PageRequest.of(page, pageSize, sort);

		// 테스트용 예약 데이터 생성
		reservations = List.of(
			createReservation(1L, "PT 상품1", LocalDate.now().plusDays(1), 14),
			createReservation(2L, "PT 상품2", LocalDate.now(), 15),
			createReservation(3L, "PT 상품3", LocalDate.now().minusDays(1), 16)
		);
	}

	@AfterEach
	void tearDown() {
		// 필요한 경우 정리 작업 수행
	}

	@Test
	@DisplayName("예약 생성 - 성공")
	void registerSuccess() {
		// given
		Long userId = 1L;
		Long ptProductId = 1L;
		ReservationRequest request = new ReservationRequest(
			null,                   // reservationId
			"PT 상품1",             // productName
			LocalDate.now().plusDays(1), // date
			14,                     // time
			100000L,               // price
			null,                  // cancelDate
			null                   // completedDate
		);

		PtProduct ptProduct = PtProduct.builder()
			.ptProductId(ptProductId)
			.ptProductName("PT 상품1")
			.trainerId(2L)
			.ptProductFee(100000L)
			.build();

		Reservation savedReservation = Reservation.builder()
			.reservationId(1L)
			.productName(ptProduct.getPtProductName())
			.date(request.date())
			.time(request.time())
			.price(ptProduct.getPtProductFee())
			.memberId(userId)
			.trainerId(ptProduct.getTrainerId())
			.build();

		// when
		when(ptProductService.findProductOrThrow(ptProductId)).thenReturn(ptProduct);
		when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

		Long result = reservationService.register(userId, ptProductId, request);

		// then
		assertAll(
			() -> assertThat(result).isEqualTo(savedReservation.getReservationId()),
			() -> verify(ptProductService).findProductOrThrow(ptProductId),
			() -> verify(reservationRepository).save(any(Reservation.class))
		);
	}

	@Test
	@DisplayName("예약 생성 - PT 상품이 존재하지 않는 경우")
	void registerWithNonExistentProduct() {
		// given
		Long userId = 1L;
		Long ptProductId = 999L;
		ReservationRequest request = new ReservationRequest(
			null,
			"존재하지 않는 상품",
			LocalDate.now().plusDays(1),
			14,
			100000L,
			null,
			null
		);

		// when
		when(ptProductService.findProductOrThrow(ptProductId))
			.thenThrow(new EntityNotFoundException("존재하지 않는 PT 상품입니다."));

		// then
		assertThrows(EntityNotFoundException.class, () ->
			reservationService.register(userId, ptProductId, request)
		);
		verify(ptProductService).findProductOrThrow(ptProductId);
		verify(reservationRepository, never()).save(any(Reservation.class));
	}
	
	@Test
	@DisplayName("회원의 예약 목록 조회 - 성공")
	void getReservationsSuccess() {
		// given
		Page<Reservation> reservationPage = new PageImpl<>(
			reservations,
			pageRequest,
			reservations.size()
		);

		when(reservationRepository.findByMemberId(memberId, pageRequest))
			.thenReturn(reservationPage);

		// when
		Page<ReservationResponse> result = reservationService.getReservations(
			memberId,
			page,
			pageSize
		);

		// then
		assertAll(
			() -> assertThat(result).isNotNull(),
			() -> assertThat(result.getContent()).hasSize(3),
			() -> assertThat(result.getContent().get(0).date())
				.isAfter(result.getContent().get(1).date()),
			() -> assertThat(result.getTotalElements()).isEqualTo(3),
			() -> assertThat(result.getNumber()).isEqualTo(page),
			() -> assertThat(result.getSize()).isEqualTo(pageSize)
		);

		verify(reservationRepository).findByMemberId(memberId, pageRequest);
		verifyNoMoreInteractions(reservationRepository);
	}

	@Test
	@DisplayName("회원의 예약 목록 조회 - 빈 결과")
	void getReservationsEmpty() {
		// given
		Page<Reservation> emptyPage = new PageImpl<>(
			List.of(),
			pageRequest,
			0
		);

		when(reservationRepository.findByMemberId(memberId, pageRequest))
			.thenReturn(emptyPage);

		// when
		Page<ReservationResponse> result = reservationService.getReservations(
			memberId,
			page,
			pageSize
		);

		// then
		assertAll(
			() -> assertThat(result).isNotNull(),
			() -> assertThat(result.getContent()).isEmpty(),
			() -> assertThat(result.getTotalElements()).isZero(),
			() -> assertThat(result.getTotalPages()).isZero()
		);

		verify(reservationRepository).findByMemberId(memberId, pageRequest);
		verifyNoMoreInteractions(reservationRepository);
	}

	// 테스트용 Reservation 객체 생성 헬퍼 메서드
	private Reservation createReservation(
		Long id,
		String productName,
		LocalDate date,
		Integer time
	) {
		return Reservation.builder()
			.reservationId(id)
			.productName(productName)
			.date(date)
			.time(time)
			.price(100000L)
			.memberId(memberId)
			.trainerId(1L)
			.build();
	}

	private Reservation createTestReservation(Long id, String productName, LocalDate date) {
		return Reservation.builder()
			.reservationId(id)
			.productName(productName)
			.date(date)
			.time(14)
			.price(100000L)
			.memberId(memberId)
			.trainerId(1L)
			.build();
	}
}