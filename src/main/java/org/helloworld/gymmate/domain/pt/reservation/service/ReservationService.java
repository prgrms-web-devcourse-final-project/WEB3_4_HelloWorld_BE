package org.helloworld.gymmate.domain.pt.reservation.service;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.pt.classtime.service.ClasstimeService;
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
	private final ClasstimeService classtimeService;

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

		// 2) classTime에 존재하는지 확인 -> 프론트에서 예약가능한 시간만 보여주므로, 일단 생략

		// 3) 예약 엔티티 생성
		Reservation reservation = ReservationMapper.toEntity(ptProduct, request, userId);

		// 3) 예약 테이블에 저장 및 ID 반환
		return reservationRepository.save(reservation).getReservationId();
	}

	/*
	 회원의 예약 삭제 로직
	  - param : reservationId
	 */
	public void deleteMemberReservation(Long reservationId) {
		//1. 예약 객체 조회
		Reservation reservation = findReservationOrThrow(reservationId);

		//2. 예약 객체 삭제
		reservationRepository.delete(reservation);

	}

	// 예약 조회 메서드 분리
	private Reservation findReservationOrThrow(Long reservationId) {
		return reservationRepository.findById(reservationId)
			.orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
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
	  트레이너의 예약 목록 조회 로직
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
