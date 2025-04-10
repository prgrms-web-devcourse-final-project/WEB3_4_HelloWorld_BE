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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "트레이너 정보 API", description = "트레이너 or 헬스장 운영자 정보에 대한 등록, 수정, 삭제, 일반 목록 및 검색 목록 조회")
@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @Operation(summary = "[정보 등록 전 트레이너] 트레이너 선생님 회원가입", description = "트레이너 선생님(트레이너 직원)의 정보를 최초로 등록")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody @Valid TrainerRegisterRequest registerRequest) {
        return ResponseEntity.ok()
            .body(trainerService.registerInfoByTrainer(trainerService.findByUserId(customOAuth2User.getUserId()),
                registerRequest));
    }

    @Operation(summary = "[정보 등록 전 트레이너] 헬스장 운영자 회원가입", description = "제휴 헬스장 운영자(트레이너 사장)의 정보를 최초로 등록")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @PostMapping("/owner")
    public ResponseEntity<Long> register(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody @Valid OwnerRegisterRequest registerRequest) {
        return ResponseEntity.ok()
            .body(trainerService.registerInfoByOwner(trainerService.findByUserId(customOAuth2User.getUserId()),
                registerRequest));
    }

    @Operation(summary = "[트레이너] 트레이너 개인 정보 수정", description = "트레이너 선생님 또는 헬스장 운영자가 자신의 개인 정보를 수정")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @PutMapping("/modify")
    public ResponseEntity<Long> modify(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestPart("request") TrainerModifyRequest modifyRequest,
        @RequestPart(value = "image", required = false) @ValidImageFile MultipartFile profile) {
        return ResponseEntity.ok()
            .body(trainerService.modifyTrainerInfo(trainerService.findByUserId(customOAuth2User.getUserId()),
                modifyRequest, profile));
    }

    @Operation(summary = "[트레이너] 트레이너 계정 삭제", description = "트레이너 선생님 또는 헬스장 운영자가 자신의 계정을 삭제")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        trainerService.deleteTrainer(trainerService.findByUserId(customOAuth2User.getUserId()));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[트레이너] 트레이너 프로필 정보 수정", description = "트레이너 선생님 또는 헬스장 운영자가 자신의 한줄소개, 경력, 전문 분야를 수정")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @PutMapping("/profile")
    public ResponseEntity<Long> updateProfile(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody TrainerProfileRequest profileRequest) {
        return ResponseEntity.ok()
            .body(trainerService.updateTrainerProfile(trainerService.findByUserId(customOAuth2User.getUserId()),
                profileRequest));
    }

    @Operation(summary = "[트레이너] 개인 정보 조회", description = "트레이너 선생님 또는 헬스장 운영자가  자신의 개인 정보를 조회")
    @GetMapping
    public ResponseEntity<TrainerResponse> getInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok()
            .body(trainerService.getInfo(trainerService.findByUserId(customOAuth2User.getUserId())));
    }

    @Operation(summary = "사장 여부 체크", description = "요청한 사용자가 헬스장 운영자인지 아닌지 반환")
    @GetMapping("/check")
    public ResponseEntity<TrainerCheckResponse> check(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok()
            .body(trainerService.check(trainerService.findByUserId(customOAuth2User.getUserId())));
    }

    @Operation(summary = "트레이너 목록 조회", description = "요청한 값에 해당하는 트레이너 목록을 조회")
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

    @Operation(summary = "00 주변 트레이너 목록 조회", description = "요청한 위치를 기준으로 가까운 트레이너 중 요청한 값에 해당하는 목록을 조회")
    @GetMapping("/list/nearby")
    @Validated
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<PageDto<TrainerListResponse>> getTrainerList(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @Parameter(
            name = "searchOption",
            description = "검색 옵션",
            schema = @Schema(allowableValues = {"trainer", "district"}, example = "trainer")
        )
        // @Parameter(description = "검색 옵션", examples = {
        //     @ExampleObject(name = "트레이너 이름", value = "trainer"), @ExampleObject(name = "헬스장 지역", value = "district")})
        @RequestParam(required = false) String searchOption,
        @Parameter(description = "검색어", examples = {
            @ExampleObject(name = "트레이너 이름", value = "trainer"), @ExampleObject(name = "헬스장 지역", value = "district")})
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
    ) {
        return ResponseEntity.ok(PageMapper.toPageDto(
            trainerService.getNearbyTrainers(searchOption, searchTerm, page, pageSize, customOAuth2User)));
    }
}
