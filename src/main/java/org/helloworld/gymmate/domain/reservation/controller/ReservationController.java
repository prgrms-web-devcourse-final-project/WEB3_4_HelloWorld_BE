package org.helloworld.gymmate.domain.reservation.controller;

import org.helloworld.gymmate.domain.reservation.dto.ReservationRequest;
import org.helloworld.gymmate.domain.reservation.service.ReservationService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@PostMapping("/ptProduct/{ptProductId}")
	public ResponseEntity<Long> register(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long ptProductId,
		@RequestBody ReservationRequest request
	) {
		return ResponseEntity.ok()
			.body(reservationService.register(
				customOAuth2User.getUserId(),
				ptProductId,
				request
			));
	}

}
