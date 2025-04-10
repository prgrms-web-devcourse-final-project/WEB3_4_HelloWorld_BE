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

@Tag(name = "헬스장 API", description = "헬스장 지도 페이지")
@RestController
@RequestMapping("/gym")
@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;

    @Operation(summary = "[일반회원] 헬스장 전체 조회 및 검색", description = "헬스장 목록을 다양한 조건(정렬, 검색, 제휴 여부, 위치 정보)에 따라 조회하며, 결과는 페이지 단위로 반환됩니다.")
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

    @Operation(summary = "[일반회원] 가까운 헬스장 조회 및 검색", description = "회원가입 시 입력한 주소를 기준으로 가까운 헬스장 목록을 반환합니다.")
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

    @Operation(summary = "[일반회원] 해당 헬스장 기본정보 조회", description = "해당 헬스장의 기본 정보와 헬스장 이용권 정보를 반환합니다. - 홈 탭")
    @GetMapping("/{gymId}")
    public ResponseEntity<GymDetailResponse> getGymDetail(
        @PathVariable Long gymId
    ) {
        return ResponseEntity.ok(gymService.getDetail(gymId));
    }

    @Operation(summary = "[일반회원] 해당 헬스장 운동기구와 편의시설 조회", description = "해당 헬스장의 운동기구와 편의시설을 반환합니다. - 시설 탭")
    @GetMapping("/{gymId}/facility")
    public ResponseEntity<FacilityAndMachineResponse> getFacilitiesAndMachines(
        @PathVariable Long gymId
    ) {
        return ResponseEntity.ok(gymService.getOwnFacilitiesAndMachines(gymId));
    }

    @Operation(summary = "[일반화원] 해당 헬스장에 소속된 트레이너 정보 조회", description = "해당 헬스장에 소속된 트레이너의 정보를 반환합니다. - 강사 탭")
    @GetMapping("/{gymId}/trainer")
    public ResponseEntity<List<TrainerDetailResponse>> getTrainerDetail(
        @PathVariable Long gymId
    ) {
        return ResponseEntity.ok(gymService.getTrainerDetail(gymId));
    }

    @Operation(summary = "[트레이너] 소속 헬스장 조회", description = "헬스장 목록을 검색어 기준으로 조회합니다. 검색어가 비어있으면 전체 헬스장을 반환합니다. ")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<PageDto<GymSearchResponse>> getSearch(
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
    ) {
        return ResponseEntity.ok(PageMapper.toPageDto(gymService.getSearch(searchTerm, page, pageSize)));
    }

}
