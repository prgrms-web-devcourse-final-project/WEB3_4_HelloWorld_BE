package org.helloworld.gymmate.domain.gym.gymTicket.controller;

import org.helloworld.gymmate.domain.gym.gymTicket.dto.GymTicketPurchaseResponse;
import org.helloworld.gymmate.domain.gym.gymTicket.service.GymTicketService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gymTicket")
@RequiredArgsConstructor
public class GymTicketController {

	private final GymTicketService gymTicketService;

	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@GetMapping("/purchase/{gymProductId}")
	public ResponseEntity<GymTicketPurchaseResponse> checkPurchaseAvailability(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long gymProductId
	) {
		return ResponseEntity.ok().body(
			gymTicketService.getPurchaseData(customOAuth2User.getUserId(), gymProductId));
	}

	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@PostMapping("/purchase/{gymProductId}")
	public ResponseEntity<Long> buyGymProduct(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long gymProductId
	) {
		return ResponseEntity.ok().body(
			gymTicketService.createTicket(customOAuth2User.getUserId(), gymProductId));
	}
}
