package org.helloworld.gymmate.domain.gym.partnergym.controller;

import java.util.List;

import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineResponse;
import org.helloworld.gymmate.domain.gym.machine.service.MachineService;
import org.helloworld.gymmate.domain.gym.partnergym.dto.request.GymRegisterRequest;
import org.helloworld.gymmate.domain.gym.partnergym.dto.request.GymUpdateRequest;
import org.helloworld.gymmate.domain.gym.partnergym.dto.response.PartnerGymDetailResponse;
import org.helloworld.gymmate.domain.gym.partnergym.service.PartnerGymService;
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

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "운영중인 제휴 헬스장 등록", description = "트레이너 사장이 아직 제휴 헬스장을 등록하지 않았을 때 마이페이지에서 제휴 헬스장 등록 요청 처리")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> registerPartnerGym(
        @RequestPart("request") @Valid GymRegisterRequest request,
        @RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images,
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(partnerGymService.registerPartnerGym(request, images, customOAuth2User.getUserId()));
    }

    @Operation(summary = "운영중인 제휴 헬스장 정보 수정", description = "트레이너 사장이 제휴 헬스장을 등록한 후 마이페이지에서 제휴 헬스장 정보 수정 요청 처리")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updatePartnerGym(
        @RequestPart("request") @Valid GymUpdateRequest request,
        @RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images,
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        return ResponseEntity.ok(
            partnerGymService.updatePartnerGym(request, images, customOAuth2User.getUserId()));
    }

    @Operation(summary = "운영중인 제휴 헬스장 정보 조회", description = "트레이너 사장이 제휴 헬스장을 등록한 후 마이페이지로 이동했을 때 띄워줄 제휴 헬스장 정보")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @GetMapping
    public ResponseEntity<PartnerGymDetailResponse> getPartnerGymDetail(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseEntity.ok(
            partnerGymService.getPartnerGymDetail(customOAuth2User.getUserId()));
    }

    @Operation(summary = "운영중인 제휴 헬스장의 운동 기구 목록 조회", description = "트레이너 사장이 제휴 헬스장을 등록한 후 마이페이지에서 보여줄 운동 기구 목록")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @GetMapping("/machine")
    public ResponseEntity<List<MachineResponse>> getMachines(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        return ResponseEntity.ok(machineService.getOwnMachines(customOAuth2User.getUserId()));
    }
}
