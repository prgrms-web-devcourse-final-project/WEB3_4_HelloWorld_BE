package org.helloworld.gymmate.domain.gym.gyminfo.controller;

import java.util.List;

import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.domain.gym.gyminfo.dto.response.GymDetailResponse;
import org.helloworld.gymmate.domain.gym.gyminfo.dto.response.GymListResponse;
import org.helloworld.gymmate.domain.gym.gyminfo.dto.response.TrainerDetailResponse;
import org.helloworld.gymmate.domain.gym.gyminfo.service.GymService;
import org.helloworld.gymmate.domain.gym.machine.dto.FacilityAndMachineResponse;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "헬스장 API", description = "헬스장 지도 페이지")
@RestController
@RequestMapping("/gym")
@RequiredArgsConstructor
public class GymController {
	private final GymService gymService;

	// 헬스장 전체 조회 & 검색
	@GetMapping
	public ResponseEntity<PageDto<GymListResponse>> getGyms(@RequestParam(defaultValue = "score") String sortOption,
		@RequestParam(required = false) String searchOption,
		@RequestParam(defaultValue = "") String searchTerm,
		@RequestParam(defaultValue = "0") @Min(0) int page,
		@RequestParam(defaultValue = "6") @Min(1) @Max(50) int pageSize,
		@RequestParam(required = false, defaultValue = "127.0276") Double x,
		@RequestParam(required = false, defaultValue = "37.4979") Double y,
		@RequestParam(required = false, defaultValue = "") Boolean isPartner
	) {
		return ResponseEntity.ok(PageMapper.toPageDto(
			gymService.getGyms(sortOption, searchOption, searchTerm, page, pageSize, x, y, isPartner)));
	}

	// 회원 가까운 헬스장 조회 & 검색
	@GetMapping("/nearby")
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public ResponseEntity<PageDto<GymListResponse>> getNearByGyms(
		@RequestParam(required = false) String searchOption,
		@RequestParam(defaultValue = "") String searchTerm,
		@RequestParam(defaultValue = "0") @Min(0) int page,
		@RequestParam(defaultValue = "6") @Min(1) @Max(50) int pageSize,
		@RequestParam(required = false, defaultValue = "") Boolean isPartner,
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User
	) {
		return ResponseEntity.ok(PageMapper.toPageDto(
			gymService.getNearbyGyms(searchOption, searchTerm, page, pageSize, isPartner, customOAuth2User)));
	}

	@GetMapping("/{gymId}")
	public ResponseEntity<GymDetailResponse> getGymDetail(
		@PathVariable Long gymId
	) {
		return ResponseEntity.ok(gymService.getDetail(gymId));
	}

	@GetMapping("/{gymId}/facility")
	public ResponseEntity<FacilityAndMachineResponse> getFacilitiesAndMachines(
		@PathVariable Long gymId
	) {
		return ResponseEntity.ok(gymService.getOwnFacilitiesAndMachines(gymId));
	}

	//헬스장 소속 트레이너 정보 전체 조회
	@GetMapping("/{gymId}/trainer")
	public ResponseEntity<List<TrainerDetailResponse>> getTrainerDetail(
		@PathVariable Long gymId
	) {
		return ResponseEntity.ok(gymService.getTrainerDetail(gymId));
	}

}
