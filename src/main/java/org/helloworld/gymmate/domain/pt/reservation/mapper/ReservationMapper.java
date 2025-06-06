package org.helloworld.gymmate.domain.pt.reservation.mapper;

import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationRequest;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationResponse;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationTrainerResponse;
import org.helloworld.gymmate.domain.pt.reservation.entity.Reservation;
import org.helloworld.gymmate.domain.pt.student.entity.Student;

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

    public static ReservationTrainerResponse toDto(Reservation reservation, Student student) {
        return new ReservationTrainerResponse(
            reservation.getReservationId(),
            reservation.getProductName(),
            reservation.getDate(),
            reservation.getTime(),
            reservation.getPrice(),
            reservation.getCancelDate(),
            reservation.getCompletedDate(),
            student != null ? student.getStudentId() : null,
            student != null ? student.getName() : null
        );
    }
}
