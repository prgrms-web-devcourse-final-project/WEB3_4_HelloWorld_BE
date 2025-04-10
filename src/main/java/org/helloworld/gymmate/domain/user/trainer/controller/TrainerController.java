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

@Tag(name = "트레이너 정보 API", description = "트레이너(선생님 & 제휴 헬스장 운영자) 정보에 대한 등록, 수정, 삭제, 일반 목록 및 검색 목록 조회")
@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @Operation(summary = "[정보 등록 전 트레이너] 트레이너 선생님 회원가입", description = "요청한 트레이너 선생님(트레이너 직원) 자신의 정보를 최초로 등록")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> register(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody @Valid TrainerRegisterRequest registerRequest
    ) {
        Long trainerId = trainerService.registerTrainerInfo(customOAuth2User.getUserId(), registerRequest);
        return ResponseEntity.ok().body(trainerId);
    }

    @Operation(summary = "[정보 등록 전 트레이너] 헬스장 운영자 회원가입", description = "요청한 제휴 헬스장 운영자(트레이너 사장) 자신의 정보를 최초로 등록")
    @PostMapping("/owner")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> register(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody @Valid OwnerRegisterRequest registerRequest
    ) {
        Long trainerId = trainerService.registerOwnerInfo(customOAuth2User.getUserId(), registerRequest);
        return ResponseEntity.ok().body(trainerId);
    }

    @Operation(summary = "[트레이너] 트레이너 개인 정보 수정", description = "요청한 트레이너 선생님 또는 헬스장 운영자 자신의 개인 정보를 수정")
    @PutMapping("/modify")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> modify(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestPart("request") TrainerModifyRequest modifyRequest,
        @RequestPart(value = "image", required = false) @ValidImageFile MultipartFile profile
    ) {
        Long trainerId = trainerService.modifyTrainerInfo(customOAuth2User.getUserId(), modifyRequest, profile);
        return ResponseEntity.ok().body(trainerId);
    }

    @Operation(summary = "[트레이너] 트레이너 프로필 정보 수정", description = "요청한 트레이너 선생님 또는 헬스장 운영자 자신의 한줄소개, 경력, 전문 분야를 수정")
    @PutMapping("/profile")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> updateProfile(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody TrainerProfileRequest profileRequest
    ) {
        Long trainerId = trainerService.updateTrainerProfile(customOAuth2User.getUserId(), profileRequest);
        return ResponseEntity.ok().body(trainerId);
    }

    @Operation(summary = "[트레이너] 트레이너 계정 삭제", description = "요청한 트레이너 선생님 또는 헬스장 운영자 자신의 계정을 삭제")
    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Void> delete(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        trainerService.deleteTrainer(customOAuth2User.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[트레이너] 개인 정보 조회", description = "요청한 트레이너 선생님 또는 헬스장 운영자 자신의 개인 정보를 조회")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<TrainerResponse> getInfo(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        TrainerResponse response = trainerService.getTrainerInfo(customOAuth2User.getUserId());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "[로그인한 사용자] 사장 여부 체크", description = "요청한 사용자가 헬스장 운영자인지 아닌지 반환")
    @GetMapping("/check")
    public ResponseEntity<TrainerCheckResponse> check(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        TrainerCheckResponse response = trainerService.checkUserTypeAndOwner(customOAuth2User.getUserId());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "트레이너 목록 조회", description = "요청한 값에 해당하는 트레이너 목록을 조회")
    @GetMapping("/list")
    @Validated
    public ResponseEntity<PageDto<TrainerListResponse>> getTrainerList(
        @Parameter(
            name = "sortOption",
            description = "정렬 옵션",
            schema = @Schema(allowableValues = {"latest", "score", "nearby"}, example = "score", defaultValue = "score")
        )
        @RequestParam(defaultValue = "score") String sortOption,
        @Parameter(
            name = "searchOption",
            description = "검색 옵션",
            schema = @Schema(allowableValues = {"trainer", "district"}, example = "trainer")
        )
        @RequestParam(required = false) String searchOption,
        @Parameter(description = "검색어", examples = {
            @ExampleObject(name = "트레이너 이름", value = "박선생"), @ExampleObject(name = "헬스장 지역", value = "판교")})
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize,
        @RequestParam(required = false, defaultValue = "127.0276") Double x,
        @RequestParam(required = false, defaultValue = "37.4979") Double y
    ) {
        PageDto<TrainerListResponse> pageResponse = PageMapper.toPageDto(
            trainerService.getTrainers(sortOption, searchOption, searchTerm, page, pageSize, x, y));
        return ResponseEntity.ok().body(pageResponse);
    }

    @Operation(summary = "[일반 회원] 00 주변 트레이너 목록 조회", description = "요청한 위치를 기준으로 가까운 트레이너 중 요청한 값에 해당하는 목록을 조회")
    @GetMapping("/list/nearby")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @Validated
    public ResponseEntity<PageDto<TrainerListResponse>> getTrainerList(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @Parameter(
            name = "searchOption",
            description = "검색 옵션",
            schema = @Schema(allowableValues = {"trainer", "district"}, example = "trainer")
        )
        @RequestParam(required = false) String searchOption,
        @Parameter(description = "검색어", examples = {
            @ExampleObject(name = "트레이너 이름", value = "박선생"), @ExampleObject(name = "헬스장 지역", value = "판교")})
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
    ) {
        PageDto<TrainerListResponse> pageResponse = PageMapper.toPageDto(
            trainerService.getNearbyTrainers(searchOption, searchTerm, page, pageSize, customOAuth2User.getUserId()));
        return ResponseEntity.ok(pageResponse);
    }
}
