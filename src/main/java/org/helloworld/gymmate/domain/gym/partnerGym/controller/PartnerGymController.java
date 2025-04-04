package org.helloworld.gymmate.domain.gym.partnerGym.controller;

import java.util.List;

import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineResponse;
import org.helloworld.gymmate.domain.gym.machine.service.MachineService;
import org.helloworld.gymmate.domain.gym.partnerGym.dto.request.GymRegisterRequest;
import org.helloworld.gymmate.domain.gym.partnerGym.dto.request.GymUpdateRequest;
import org.helloworld.gymmate.domain.gym.partnerGym.dto.response.PartnerGymDetailResponse;
import org.helloworld.gymmate.domain.gym.partnerGym.service.PartnerGymService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "제휴 헬스장 API", description = "헬스장 운영자 마이페이지")
@RestController
@RequestMapping("/partnergym")
@RequiredArgsConstructor
public class PartnerGymController {

	private final PartnerGymService partnerGymService;
	private final MachineService machineService;

	// 제휴 헬스장 등록
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Long> registerPartnerGym(
		@RequestPart("request") @Valid GymRegisterRequest request,
		@RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images,
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(partnerGymService.registerPartnerGym(request, images, customOAuth2User.getUserId()));
	}

	// 제휴 헬스장 수정
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Long> updatePartnerGym(
		@RequestPart("request") @Valid GymUpdateRequest request,
		@RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images,
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

		return ResponseEntity.ok(
			partnerGymService.updatePartnerGym(request, images, customOAuth2User.getUserId()));
	}

	// 제휴 헬스장 조회
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@GetMapping
	public ResponseEntity<PartnerGymDetailResponse> getPartnerGymDetail(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User
	) {
		return ResponseEntity.ok(
			partnerGymService.getPartnerGymDetail(customOAuth2User.getUserId()));
	}

	// 제휴 헬스장 머신 리스트 조회
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@GetMapping("/machine")
	public ResponseEntity<List<MachineResponse>> getMachines(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

		return ResponseEntity.ok(machineService.getOwnMachines(customOAuth2User.getUserId()));
	}
}
