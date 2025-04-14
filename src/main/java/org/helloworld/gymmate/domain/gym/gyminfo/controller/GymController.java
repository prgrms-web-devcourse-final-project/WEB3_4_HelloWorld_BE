package org.helloworld.gymmate.domain.gym.gyminfo.controller;

import java.util.List;

import org.helloworld.gymmate.common.dto.PageDto;
import org.helloworld.gymmate.common.mapper.PageMapper;
import org.helloworld.gymmate.domain.gym.gyminfo.dto.response.GymDetailResponse;
import org.helloworld.gymmate.domain.gym.gyminfo.dto.response.GymListResponse;
import org.helloworld.gymmate.domain.gym.gyminfo.dto.response.GymSearchResponse;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "헬스장 API", description = "헬스장 (제휴 헬스장 포함) 상세 정보 및 목록 조회")
@RestController
@RequestMapping("/gym")
@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;

    @Operation(summary = "헬스장 전체 목록 조회 및 검색", description = "헬스장 목록을 다양한 조건(정렬, 검색, 제휴 여부, 위치 정보)에 따라 조회하며, 결과는 페이지 단위로 반환")
    @GetMapping
    public ResponseEntity<PageDto<GymListResponse>> getGyms(@RequestParam(defaultValue = "score") String sortOption,
        @RequestParam(defaultValue = "NONE") String searchOption,
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "6") @Min(1) @Max(200) int pageSize,
        @RequestParam(required = false, defaultValue = "127.0276") Double x,
        @RequestParam(required = false, defaultValue = "37.4979") Double y,
        @RequestParam(required = false, defaultValue = "") Boolean isPartner
    ) {
        PageDto<GymListResponse> pageResponse = PageMapper.toPageDto(
            gymService.getGyms(sortOption, searchOption, searchTerm, page, pageSize, x, y, isPartner));
        return ResponseEntity.ok().body(pageResponse);
    }

    @Operation(summary = "[일반 회원] 가까운 헬스장 목록 조회 및 검색", description = "회원가입 시 입력한 주소를 기준으로 가까운 헬스장 목록을 반환")
    @GetMapping("/nearby")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<PageDto<GymListResponse>> getNearByGyms(
        @RequestParam(required = false) String searchOption,
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "6") @Min(1) @Max(200) int pageSize,
        @RequestParam(required = false, defaultValue = "") Boolean isPartner,
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        PageDto<GymListResponse> pageResponse = PageMapper.toPageDto(
            gymService.getNearbyGyms(searchOption, searchTerm, page, pageSize, isPartner, customOAuth2User));
        return ResponseEntity.ok().body(pageResponse);
    }

    @Operation(summary = "선택한 헬스장의 기본정보 조회", description = "선택한 헬스장의 기본 정보와 헬스장 이용권 정보를 반환 (헬스장 페이지 - 홈 탭)")
    @GetMapping("/{gymId}")
    public ResponseEntity<GymDetailResponse> getGymDetail(
        @PathVariable Long gymId
    ) {
        GymDetailResponse response = gymService.getDetail(gymId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "선택한 헬스장의 운동기구 및 편의시설 조회", description = "선택한 헬스장의 운동기구 목록과 편의시설 정보를 반환 (헬스장 페이지 - 시설 탭)")
    @GetMapping("/{gymId}/facility")
    public ResponseEntity<FacilityAndMachineResponse> getFacilitiesAndMachines(
        @PathVariable Long gymId
    ) {
        FacilityAndMachineResponse response = gymService.getOwnFacilitiesAndMachines(gymId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "선택한 헬스장의 소속 트레이너 정보 조회", description = "선택한 헬스장에 소속된 트레이너 목록을 반환 (헬스장 페이지 - 강사 탭)")
    @GetMapping("/{gymId}/trainer")
    public ResponseEntity<List<TrainerDetailResponse>> getTrainers(
        @PathVariable Long gymId
    ) {
        List<TrainerDetailResponse> responses = gymService.getTrainerDetail(gymId);
        return ResponseEntity.ok().body(responses);
    }

    @Operation(summary = "[트레이너] 소속될 헬스장 조회", description = "트레이너 본인이 소속될 헬스장을 조회, 검색어 기준으로 조회하며, 검색어가 비어있으면 전체 헬스장 목록 반환")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<PageDto<GymSearchResponse>> getGymForTrainer(
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
    ) {
        PageDto<GymSearchResponse> pageResponse = PageMapper.toPageDto(
            gymService.getSearch(searchTerm, page, pageSize));
        return ResponseEntity.ok().body(pageResponse);
    }

    @Operation(summary = "[헬스장 운영자] 주인될 헬스장 조회", description = "주인될 헬스장을 조회, 검색어 기준으로 조회하며, 검색어가 비어있으면 전체 헬스장 목록 반환, 이미 주인이 등록된 헬스장은 제외")
    @GetMapping("/search/owner")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<PageDto<GymSearchResponse>> getGymForOwner(
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
    ) {
        PageDto<GymSearchResponse> pageResponse = PageMapper.toPageDto(
            gymService.getAvailablePartnerGymSearch(searchTerm, page, pageSize));
        return ResponseEntity.ok().body(pageResponse);
    }
}
