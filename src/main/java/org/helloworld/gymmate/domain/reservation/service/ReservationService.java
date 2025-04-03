package org.helloworld.gymmate.domain.reservation.service;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.pt_product.repository.PtProductRepository;
import org.helloworld.gymmate.domain.reservation.dto.ReservationRequest;
import org.helloworld.gymmate.domain.reservation.entity.Reservation;
import org.helloworld.gymmate.domain.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final PtProductRepository ptProductRepository;

	/*
	 예약 생성 로직
	 1. ptProduct_id로 ptProduct 객체 조회
	 2. ptProduct 정보, reservationRequest 정보, member_id 로 reservation객체 생성&저장
	  - ptProduct 객체 에서 ptProductName,TrainerId,ptProductFee 조회
	 */
	@Transactional
	public Long register(Long userId, Long ptProductId, ReservationRequest request) {
		// 1) PT 상품 조회
		PtProduct ptProduct = ptProductRepository.findById(ptProductId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PTPRODUCT_NOT_FOUND));

		// 2) 예약 엔티티 생성
		Reservation reservation = Reservation.builder()
			.productName(ptProduct.getPtProductName())        // 스냅샷
			.trainerId(ptProduct.getTrainerId())     // 스냅샷
			.date(request.date())
			.time(request.time())
			.price(ptProduct.getPtProductFee())      // 스냅샷
			.memberId(userId)
			.build();

		// 3) 저장 및 ID 반환
		return reservationRepository.save(reservation).getReservationId();
	}
}
