package org.helloworld.gymmate.domain.user.trainer.controller;

import org.helloworld.gymmate.domain.user.trainer.dto.OwnerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.mapper.TrainerMapper;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

	private final TrainerService trainerService;

	// 직원 추가 정보 등록
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PostMapping
	public ResponseEntity<Void> test(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestBody @Valid TrainerRegisterRequest registerRequest){
		trainerService.registerInfoByTrainer(trainerService.findByUserId(customOAuth2User.getUserId()), registerRequest);
		return ResponseEntity.ok().build();
	}

	// 직원 추가 정보 등록
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PostMapping("/owner")
	public ResponseEntity<Void> test(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestBody @Valid OwnerRegisterRequest registerRequest){
		trainerService.registerInfoByOwner(trainerService.findByUserId(customOAuth2User.getUserId()), registerRequest);
		return ResponseEntity.ok().build();
	}
}
