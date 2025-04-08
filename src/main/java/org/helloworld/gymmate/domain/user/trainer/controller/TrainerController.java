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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "트레이너 정보 API", description = "트레이너 or 헬스장 운영자 마이페이지, 트레이너 목록 등")
@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @Operation(summary = "트레이너 직원 회원가입", description = "선생님 로그인 선택 후 헬스장 직원을 선택했을 때 회원가입")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @PostMapping
    public ResponseEntity<Long> register(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody @Valid TrainerRegisterRequest registerRequest) {
        return ResponseEntity.ok()
            .body(trainerService.registerInfoByTrainer(trainerService.findByUserId(customOAuth2User.getUserId()),
                registerRequest));
    }

    @Operation(summary = "트레이너 사장 회원가입", description = "선생님 로그인 선택 후 헬스장 사장님을 선택했을 때 회원가입")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @PostMapping("/owner")
    public ResponseEntity<Long> register(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody @Valid OwnerRegisterRequest registerRequest) {
        return ResponseEntity.ok()
            .body(trainerService.registerInfoByOwner(trainerService.findByUserId(customOAuth2User.getUserId()),
                registerRequest));
    }

    @Operation(summary = "트레이너 마이페이지 정보 수정", description = "트레이너 직원 또는 사장이 마이페이지에서 자신의 개인 정보 수정")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @PutMapping("/modify")
    public ResponseEntity<Long> modify(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestPart("request") TrainerModifyRequest modifyRequest,
        @RequestPart(value = "image", required = false) @ValidImageFile MultipartFile profile) {
        return ResponseEntity.ok()
            .body(trainerService.modifyTrainerInfo(trainerService.findByUserId(customOAuth2User.getUserId()),
                modifyRequest, profile));
    }

    @Operation(summary = "트레이너 계정 삭제", description = "트레이너 직원 또는 사장님이 자신의 계정을 삭제")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        trainerService.deleteTrainer(trainerService.findByUserId(customOAuth2User.getUserId()));
        return ResponseEntity.ok().build();
    }

    // 한줄소개, 경력, 전문 분야 입력 -> 이거 언제 쓰나요
    @Operation(summary = "???", description = "??? 페이지에서 ???")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @PutMapping("/profile")
    public ResponseEntity<Long> updateProfile(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody TrainerProfileRequest profileRequest) {
        return ResponseEntity.ok()
            .body(trainerService.updateTrainerProfile(trainerService.findByUserId(customOAuth2User.getUserId()),
                profileRequest));
    }

    @Operation(summary = "트레이너 마이페이지 정보 조회", description = "트레이너 직원 또는 사장이 마이페이지로 이동했을 때 띄워주는 정보")
    @GetMapping
    public ResponseEntity<TrainerResponse> getInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok()
            .body(trainerService.getInfo(trainerService.findByUserId(customOAuth2User.getUserId())));
    }

    @Operation(summary = "사장 여부 체크", description = "트레이너 사장인지 아닌지 반환, 메인페이지 로직에 사용")
    @GetMapping("/check")
    public ResponseEntity<TrainerCheckResponse> check(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok()
            .body(trainerService.check(trainerService.findByUserId(customOAuth2User.getUserId())));
    }

    @Operation(summary = "???? 조회", description = "???페이지에서 ???")
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

    @Operation(summary = "???? 조회", description = "???페이지에서 ???")
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
