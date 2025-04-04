package org.helloworld.gymmate.domain.gym.gymInfo.controller;

import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.gym.gymInfo.dto.request.RegisterGymRequest;
import org.helloworld.gymmate.domain.gym.gymInfo.dto.request.UpdateGymRequest;
import org.helloworld.gymmate.domain.gym.gymInfo.dto.response.PartnerGymDetailResponse;
import org.helloworld.gymmate.domain.gym.gymInfo.service.GymService;
import org.helloworld.gymmate.domain.gym.machine.dto.FacilityAndMachineResponse;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineResponse;
import org.helloworld.gymmate.domain.gym.machine.service.MachineService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
	private final MachineService machineService;

	// 제휴 헬스장 등록
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Long>> registerPartnerGym(
		@RequestPart("request") @Valid RegisterGymRequest request,
		@RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images,
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

		Long partnerGymId = gymService.registerPartnerGym(request, images, customOAuth2User.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(Map.of("partnerGymId", partnerGymId));

	}

	// 제휴 헬스장 수정
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PutMapping(value = "/partnerGym/{partnerGymId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Long> updatePartnerGym(
		@PathVariable Long partnerGymId,
		@RequestPart("request") @Valid UpdateGymRequest request,
		@RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images,
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

		return ResponseEntity.ok(
			gymService.updatePartnerGym(partnerGymId, request, images, customOAuth2User.getUserId()));
	}

	// 제휴 헬스장 조회
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@GetMapping("/partnerGym/{partnerGymId}")
	public ResponseEntity<PartnerGymDetailResponse> getPartnerGymDetail(
		@PathVariable Long partnerGymId,
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User
	) {
		return ResponseEntity.ok(
			gymService.getPartnerGymDetail(partnerGymId, customOAuth2User.getUserId()));

	}

	// 제휴 헬스장 머신 리스트 조회
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@GetMapping("/machine")
	public ResponseEntity<List<MachineResponse>> getMachines(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

		return ResponseEntity.ok(machineService.getOwnMachines(customOAuth2User.getUserId()));
	}

	@GetMapping("/{gymId}/facility")
	public ResponseEntity<FacilityAndMachineResponse> getFacilitiesAndMachines(
		@PathVariable Long gymId
	) {
		return ResponseEntity.ok(machineService.getOwnFacilitiesAndMachines(gymId));
	}
}
