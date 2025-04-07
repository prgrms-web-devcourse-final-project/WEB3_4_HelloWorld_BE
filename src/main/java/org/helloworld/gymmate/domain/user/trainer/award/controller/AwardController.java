package org.helloworld.gymmate.domain.user.trainer.award.controller;

import org.helloworld.gymmate.domain.user.trainer.award.dto.AwardRequest;
import org.helloworld.gymmate.domain.user.trainer.award.service.AwardService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/award")
@RequiredArgsConstructor
public class AwardController {
	private final AwardService awardService;

	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PostMapping
	public ResponseEntity<Long> makeAward(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestBody @Valid AwardRequest awardRequest
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(awardService.createAward(customOAuth2User.getUserId(), awardRequest));
	}

}
