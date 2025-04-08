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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

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

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> updateReservation(
        @PathVariable Long reservationId,
        @RequestBody ReservationRequest request) {

        // 예약 수정 로직
        ReservationResponse response
            = reservationService.updateReservation(reservationId, request.date(), request.time());

        return ResponseEntity.ok(response);
    }

    /*
     회원의 예약 삭제 API
     - param : reservationId
    */
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteMemberReservation(
        @PathVariable Long reservationId
    ) {
        reservationService.deleteMemberReservation(reservationId);
        return ResponseEntity.ok().build();
    }

    /*
      멤버의 예약 목록 조회 API
      컨트롤러 -> 서비스 전달 정보 : 유저id
     */
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

    /*
    트레이너의 예약 목록 조회 API
    컨트롤러 -> 서비스 전달 정보 : 유저id
   */
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

    /*
    트레이너의 월별 예약 목록 조회 API
     */
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @GetMapping("/trainer/month")
    public ResponseEntity<ReservationByMonthResponse> getTrainerReservationsByMonth(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam int year,
        @RequestParam int month
    ) {
        ReservationByMonthResponse reservations = reservationService.getTrainerReservationsByMonth(
            customOAuth2User.getUserId(),
            year,
            month
        );

        return ResponseEntity.ok(reservations);
    }

}
