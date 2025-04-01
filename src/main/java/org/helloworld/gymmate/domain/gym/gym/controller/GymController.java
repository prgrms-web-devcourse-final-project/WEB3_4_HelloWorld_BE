package org.helloworld.gymmate.domain.gym.gym.controller;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gym.dto.request.GymUpdateRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.request.PartnerGymRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.response.GymResponse;
import org.helloworld.gymmate.domain.gym.gym.dto.response.PartnerGymResponse;
import org.helloworld.gymmate.domain.gym.gym.service.GymService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	//제휴 헬스장 등록
	@PostMapping
	public ResponseEntity<PartnerGymResponse> registerPartnerGym(
		@RequestBody PartnerGymRequest request,
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User){

		Long ownerId = customOAuth2User.getUserId(); // 인증된 사용자 ID
		PartnerGymResponse response = gymService.registerPartnerGym(request, ownerId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping(value = "/{gymId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<GymResponse> updateGym(
		@PathVariable Long gymId,
		@RequestPart("request") @Valid GymUpdateRequest request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images) {

		GymResponse response = gymService.updateGym(gymId, request, images);
		return ResponseEntity.ok(response);
	}
}
