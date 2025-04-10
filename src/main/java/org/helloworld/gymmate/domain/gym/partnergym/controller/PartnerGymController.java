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

@Tag(name = "제휴 헬스장 API", description = "헬스장 운영자 자신이 운영하는 제휴 헬스장 정보 등록, 수정, 조회")
@RestController
@RequestMapping("/partnergym")
@RequiredArgsConstructor
public class PartnerGymController {
    private final PartnerGymService partnerGymService;
    private final MachineService machineService;

    @Operation(summary = "[헬스장 운영자] 운영중인 제휴 헬스장 등록", description = "트레이너 사장이 아직 제휴 헬스장을 등록하지 않은 상태에서 자신이 운영하는 제휴 헬스장 등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> registerPartnerGym(
        @RequestPart("request") @Valid GymRegisterRequest request,
        @RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images,
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Long partnerGymId = partnerGymService.registerPartnerGym(request, images, customOAuth2User.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(partnerGymId);
    }

    @Operation(summary = "[헬스장 운영자] 운영중인 제휴 헬스장 정보 수정", description = "트레이너 사장이 제휴 헬스장을 등록한 상태에서 자신이 운영하는 제휴 헬스장 정보 수정")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> updatePartnerGym(
        @RequestPart("request") @Valid GymUpdateRequest request,
        @RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images,
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Long partnerGymId = partnerGymService.updatePartnerGym(request, images, customOAuth2User.getUserId());
        return ResponseEntity.ok().body(partnerGymId);
    }

    @Operation(summary = "[헬스장 운영자] 운영중인 제휴 헬스장 정보 조회", description = "트레이너 사장이 제휴 헬스장을 등록한 상태에서 자신이 운영하는 제휴 헬스장 정보 조회")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<PartnerGymDetailResponse> getPartnerGymDetail(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        PartnerGymDetailResponse response = partnerGymService.getPartnerGymDetail(customOAuth2User.getUserId());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "[헬스장 운영자] 운영중인 제휴 헬스장의 운동 기구 목록 조회", description = "트레이너 사장이 제휴 헬스장을 등록한 상태에서 자신이 운영하는 제휴 헬스장의 운동 기구 목록 조회")
    @GetMapping("/machine")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<List<MachineResponse>> getMachines(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        List<MachineResponse> responses = machineService.getOwnMachines(customOAuth2User.getUserId());
        return ResponseEntity.ok().body(responses);
    }
}
