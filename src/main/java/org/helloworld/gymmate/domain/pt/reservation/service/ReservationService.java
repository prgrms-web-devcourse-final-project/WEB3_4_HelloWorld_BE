package org.helloworld.gymmate.domain.pt.reservation.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.ptproduct.service.PtProductService;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationRequest;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationResponse;
import org.helloworld.gymmate.domain.pt.reservation.entity.Reservation;
import org.helloworld.gymmate.domain.pt.reservation.mapper.ReservationMapper;
import org.helloworld.gymmate.domain.pt.reservation.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final PtProductService ptProductService;

	/*
	 예약 생성 로직
	 1. ptProduct_id로 ptProduct 객체 조회
	 2. ptProduct 정보, reservationRequest 정보, member_id 로 reservation객체 생성&저장
	  - ptProduct 객체 에서 ptProductName,TrainerId,ptProductFee 조회
	 */
	@Transactional
	public Long register(Long userId, Long ptProductId, ReservationRequest request) {
		// 1) PT 상품 조회
		PtProduct ptProduct = ptProductService.findProductOrThrow(ptProductId);

		// TODO: 2) classTime에서 해당 요일,시간을 '예약완료'처리

		// 3) 전달받은 요일로부터 실제 날짜 계산
		LocalDate date = calculateNextWeekDate(request.dayOfWeek());

		// 4) 실제 날짜를 기반으로 예약 엔티티 생성
		Reservation reservation = ReservationMapper.toEntity(ptProduct, request, date, userId);

		// 5) 예약 테이블에 저장 및 ID 반환
		return reservationRepository.save(reservation).getReservationId();
	}

	/*
	 다음 주 특정 요일의 날짜 계산
	 param : dayOfWeek (0: 월요일 ~ 6: 일요일)
	 return : 다음 주 해당 요일의 날짜
	 */
	private LocalDate calculateNextWeekDate(int dayOfWeek) {
		LocalDate today = LocalDate.now();

		// 오늘부터 시작하여 다음 주 해당 요일까지의 날짜 계산
		LocalDate targetDate = today
			.plusWeeks(1)  // 다음 주로 이동
			.with(TemporalAdjusters.previousOrSame(DayOfWeek.of(dayOfWeek)));  // 해당 요일로 조정

		return targetDate;
	}

	/*
	  회원의 예약 목록 조회 로직
	   - 매개변수 : 회원 ID
	   - 리턴값 : 회원의 예약 목록
	 */
	@Transactional(readOnly = true)
	public Page<ReservationResponse> getMemberReservations(
		Long memberId,
		int page,
		int pageSize
	) {
		// 정렬 조건 고정 (예약 날짜 내림차순)
		Sort sort = Sort.by(Sort.Direction.DESC, "date");

		// 페이징 요청 생성
		Pageable pageable = PageRequest.of(page, pageSize, sort);

		// 페이징된 데이터 조회 및 DTO 변환
		return reservationRepository.findByMemberId(memberId, pageable)
			.map(ReservationMapper::toDto);
	}

	/*
	  회원의 예약 목록 조회 로직
	   - 매개변수 : 회원 ID
	   - 리턴값 : 회원의 예약 목록
	 */
	@Transactional(readOnly = true)
	public Page<ReservationResponse> getTrainerReservations(
		Long trainerId,
		int page,
		int pageSize
	) {
		// 정렬 조건 고정 (예약 날짜 내림차순)
		Sort sort = Sort.by(Sort.Direction.DESC, "date");

		// 페이징 요청 생성
		Pageable pageable = PageRequest.of(page, pageSize, sort);

		// 페이징된 데이터 조회 및 DTO 변환
		return reservationRepository.findByTrainerId(trainerId, pageable)
			.map(ReservationMapper::toDto);
	}
}
