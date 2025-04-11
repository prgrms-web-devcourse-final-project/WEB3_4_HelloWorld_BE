package org.helloworld.gymmate.domain.pt.reservation.controller;

import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationByMonthResponse;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationRequest;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationResponse;
import org.helloworld.gymmate.domain.pt.reservation.service.ReservationService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "PT 예약 API", description = "PT 예약 등록, 수정, 삭제, 조회")
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    /*
     예약 생성 API

     Controller -> Service 전달 정보:
     1. ReservationRequest: 사용자가 입력한 예약 정보
     2. memberId: 예약 주체인 member의 ID (멤버 객체 조회는 서비스에서 수행)
         - customOAuth2User.getUserId()로 구함.
     3. ptProductId: 예약할 PT 상품의 ID

     Service 역할:
     1. ptProduct_id로 ptProduct 정보 조회
     2. ptProduct 정보, reservationRequest 정보, member_id 로 reservation객체 생성&저장
     */

    @Operation(summary = "멤버 - 예약 등록")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/ptProduct/{ptProductId}")
    @Validated
    public ResponseEntity<Long> register(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long ptProductId,
        @Valid @RequestBody ReservationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(reservationService.register(
                customOAuth2User.getUserId(),
                ptProductId,
                request
            ));
    }

    @Operation(summary = "멤버 - 예약 수정")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("/{reservationId}")
    public ResponseEntity<Long> updateReservation(
        @PathVariable Long reservationId,
        @RequestBody ReservationRequest request) {

        Long updateId = reservationService.updateReservation(reservationId, request.date(), request.time());

        return ResponseEntity.ok(updateId);
    }

    @Operation(summary = "멤버 - 예약 취소")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Long> deleteMemberReservation(
        @PathVariable Long reservationId
    ) {
        Long deleteId =
            reservationService.cancelMemberReservation(reservationId);
        return ResponseEntity.ok(deleteId);
    }

    @Operation(summary = "멤버 - 예약 목록 조회")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/member")
    public ResponseEntity<PageDto<ReservationResponse>> getMemberReservations(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
    ) {
        return ResponseEntity.ok()
            .body(PageMapper.toPageDto(
                reservationService.getMemberReservations(
                    customOAuth2User.getUserId(),
                    page,
                    pageSize
                )
            ));
    }

    @Operation(summary = "트레이너 - 예약 목록 조회")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @GetMapping("/trainer")
    public ResponseEntity<PageDto<ReservationResponse>> getTrainerReservations(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
    ) {
        return ResponseEntity.ok()
            .body(PageMapper.toPageDto(
                reservationService.getTrainerReservations(
                    customOAuth2User.getUserId(),
                    page,
                    pageSize
                )
            ));
    }

    @Operation(summary = "트레이너 - 월별 예약 목록 조회")
    @GetMapping("/trainer/{trainerId}/month")   // oauth대신 url로 trainerId받음
    public ResponseEntity<ReservationByMonthResponse> getTrainerReservationsByMonth(
        @PathVariable Long trainerId,
        @RequestParam int year,
        @RequestParam int month
    ) {
        ReservationByMonthResponse reservations = reservationService.getTrainerReservationsByMonth(
            trainerId,
            year,
            month
        );

        return ResponseEntity.ok(reservations);
    }

}
