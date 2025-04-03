package org.helloworld.gymmate.domain.gym.gymInfo.controller;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gymInfo.dto.request.RegisterGymRequest;
import org.helloworld.gymmate.domain.gym.gymInfo.dto.request.UpdateGymRequest;
import org.helloworld.gymmate.domain.gym.gymInfo.service.GymService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gym")
@RequiredArgsConstructor
public class GymController {

	private final GymService gymService;

	// 제휴 헬스장 등록
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Long> registerPartnerGym(
		@RequestPart("request") @Valid RegisterGymRequest request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images,
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

		return ResponseEntity.status(HttpStatus.CREATED).body(
			gymService.registerPartnerGym(request, images, customOAuth2User.getUserId()));
	}

	// 제휴 헬스장 수정
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PutMapping(value = "/partnerGym/{partnerGymId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Long> updatePartnerGym(
		@PathVariable Long partnerGymId,
		@RequestPart("request") @Valid UpdateGymRequest request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images,
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

		return ResponseEntity.ok(
			gymService.updatePartnerGym(partnerGymId, request, images, customOAuth2User.getUserId()));
	}
}
