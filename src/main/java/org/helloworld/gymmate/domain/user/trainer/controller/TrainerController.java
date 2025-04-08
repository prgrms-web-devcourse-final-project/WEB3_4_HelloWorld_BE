package org.helloworld.gymmate.domain.user.trainer.controller;

import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.user.trainer.dto.OwnerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerCheckResponse;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerListResponse;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerModifyRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerProfileRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerResponse;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

	private final TrainerService trainerService;

	// 직원 추가 정보 등록
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PostMapping
	public ResponseEntity<Long> register(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestBody @Valid TrainerRegisterRequest registerRequest) {
		return ResponseEntity.ok()
			.body(trainerService.registerInfoByTrainer(trainerService.findByUserId(customOAuth2User.getUserId()),
				registerRequest));
	}

	// 사장 추가 정보 등록
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PostMapping("/owner")
	public ResponseEntity<Long> register(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestBody @Valid OwnerRegisterRequest registerRequest) {
		return ResponseEntity.ok()
			.body(trainerService.registerInfoByOwner(trainerService.findByUserId(customOAuth2User.getUserId()),
				registerRequest));
	}

	// 직원 및 사장 개인정보 수정
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PutMapping("/modify")
	public ResponseEntity<Long> modify(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestPart("request") TrainerModifyRequest modifyRequest,
		@RequestPart(value = "image", required = false) @ValidImageFile MultipartFile profile) {
		return ResponseEntity.ok()
			.body(trainerService.modifyTrainerInfo(trainerService.findByUserId(customOAuth2User.getUserId()),
				modifyRequest, profile));
	}

	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@DeleteMapping
	public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		trainerService.deleteTrainer(trainerService.findByUserId(customOAuth2User.getUserId()));
		return ResponseEntity.ok().build();
	}

	// 한줄소개, 경력, 전문 분야 입력
	@PreAuthorize("hasRole('ROLE_TRAINER')")
	@PutMapping("/profile")
	public ResponseEntity<Long> updateProfile(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestBody TrainerProfileRequest profileRequest) {
		return ResponseEntity.ok()
			.body(trainerService.updateTrainerProfile(trainerService.findByUserId(customOAuth2User.getUserId()),
				profileRequest));
	}

	// 마이페이지 정보
	@GetMapping
	public ResponseEntity<TrainerResponse> getInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		return ResponseEntity.ok()
			.body(trainerService.getInfo(trainerService.findByUserId(customOAuth2User.getUserId())));
	}

	// 트레이너인지 check -> 메인페이지 활용
	@GetMapping("/check")
	public ResponseEntity<TrainerCheckResponse> check(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		return ResponseEntity.ok()
			.body(trainerService.check(trainerService.findByUserId(customOAuth2User.getUserId())));

	}

	// 트레이너 전체조회 & 검색
	@GetMapping("/list")
	@Validated
	public ResponseEntity<PageDto<TrainerListResponse>> getTrainerList(
		@RequestParam(defaultValue = "score") String sortOption,
		@RequestParam(required = false) String searchOption,
		@RequestParam(defaultValue = "") String searchTerm,
		@RequestParam(defaultValue = "0") @Min(0) int page,
		@RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize,
		@RequestParam(required = false, defaultValue = "127.0276") Double x,
		@RequestParam(required = false, defaultValue = "37.4979") Double y
	) {
		return ResponseEntity.ok(PageMapper.toPageDto(
			trainerService.getTrainers(sortOption, searchOption, searchTerm, page, pageSize, x, y)));
	}

	// (회원) 트레이너 가까운순 전체조회 & 검색
	@GetMapping("/list/nearby")
	@Validated
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public ResponseEntity<PageDto<TrainerListResponse>> getTrainerList(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestParam(required = false) String searchOption,
		@RequestParam(defaultValue = "") String searchTerm,
		@RequestParam(defaultValue = "0") @Min(0) int page,
		@RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
	) {
		return ResponseEntity.ok(PageMapper.toPageDto(
			trainerService.getNearbyTrainers(searchOption, searchTerm, page, pageSize, customOAuth2User)));
	}
}
