package org.helloworld.gymmate.domain.reservation.service;

import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.pt_product.service.PtProductService;
import org.helloworld.gymmate.domain.reservation.dto.ReservationRequest;
import org.helloworld.gymmate.domain.reservation.dto.ReservationResponse;
import org.helloworld.gymmate.domain.reservation.entity.Reservation;
import org.helloworld.gymmate.domain.reservation.mapper.ReservationMapper;
import org.helloworld.gymmate.domain.reservation.repository.ReservationRepository;
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
		// 2) 예약 엔티티 생성
		Reservation reservation = ReservationMapper.toEntity(ptProduct, request, userId);

		// 3) 저장 및 ID 반환
		return reservationRepository.save(reservation).getReservationId();
	}

	/*
	  회원의 예약 목록 조회 로직
	   - 매개변수 : 회원 ID
	   - 리턴값 : 회원의 예약 목록
	 */
	@Transactional(readOnly = true)
	public Page<ReservationResponse> getReservations(
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
}
