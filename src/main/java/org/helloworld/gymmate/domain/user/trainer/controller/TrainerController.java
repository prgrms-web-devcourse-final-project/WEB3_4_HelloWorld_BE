package org.helloworld.gymmate.domain.user.trainer.controller;

import org.helloworld.gymmate.domain.user.trainer.mapper.TrainerMapper;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

	private final TrainerService trainerService;

	// 트레이너 정보 테스트
	@GetMapping("/test")
	public ResponseEntity<?> test(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		return ResponseEntity.ok(TrainerMapper.toDto(trainerService.findByUserId(customOAuth2User.getUserId())));
	}

}
