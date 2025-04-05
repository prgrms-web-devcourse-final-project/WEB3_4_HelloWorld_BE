package org.helloworld.gymmate.domain.reservation.mapper;

import org.helloworld.gymmate.domain.pt.ptProduct.entity.PtProduct;
import org.helloworld.gymmate.domain.reservation.dto.ReservationRequest;
import org.helloworld.gymmate.domain.reservation.dto.ReservationResponse;
import org.helloworld.gymmate.domain.reservation.entity.Reservation;

public class ReservationMapper {

	/*
	 PT 상품과 예약 요청과 유저Id로부터 예약 엔티티 생성
	 예약 시점의 PT 상품 정보를 스냅샷으로 저장
	 */
	public static Reservation toEntity(
		PtProduct ptProduct,
		ReservationRequest request,
		Long userId
	) {
		return Reservation.builder()
			.productName(ptProduct.getPtProductName())  // 스냅샷
			.trainerId(ptProduct.getTrainerId())       // 스냅샷
			.date(request.date())
			.time(request.time())
			.price(ptProduct.getPtProductFee())        // 스냅샷
			.memberId(userId)
			.build();
	}

	public static ReservationResponse toDto(Reservation reservation) {
		return new ReservationResponse(
			reservation.getReservationId(),
			reservation.getProductName(),
			reservation.getDate(),
			reservation.getTime(),
			reservation.getPrice(),
			reservation.getCancelDate(),
			reservation.getCompletedDate(),
			reservation.getTrainerId()
		);
	}
}
