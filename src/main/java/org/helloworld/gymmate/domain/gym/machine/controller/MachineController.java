package org.helloworld.gymmate.domain.gym.machine.controller;

import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineRequest;
import org.helloworld.gymmate.domain.gym.machine.service.MachineService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@Tag(name = "운동 기구 API", description = "헬스장 운영자 마이페이지 - 운동 기구")
@RestController
@RequestMapping("/machine")
@RequiredArgsConstructor
public class MachineController {
    private final MachineService machineService;

    @Operation(summary = "운동 기구 등록", description = "헬스장 운영자 마이페이지에서 제휴 헬스장 등록 또는 수정 중 운동 기구 1개 추가")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> createMachine(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestPart("machineData") @Valid MachineRequest request,
        @RequestPart(required = false) @ValidImageFile MultipartFile image
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            machineService.createMachine(customOAuth2User.getUserId(), request, image));
    }

    @Operation(summary = "운동 기구 삭제", description = "헬스장 운영자 마이페이지에서 제휴 헬스장 등록 또는 수정 중 운동 기구 1개 제거")
    @DeleteMapping("/{machineId}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Void> deleteMachine(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long machineId
    ) {
        machineService.deleteMachines(customOAuth2User.getUserId(), machineId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "운동 기구 수정", description = "헬스장 운영자 마이페이지에서 제휴 헬스장 등록 또는 수정 중 운동 기구 1개 정보 수정")
    @PutMapping("/{machineId}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> modifyMachine(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestPart("machineData") @Valid MachineRequest request,
        @RequestPart(required = false) @ValidImageFile MultipartFile image,
        @PathVariable Long machineId
    ) {
        return ResponseEntity.ok(
            machineService.modifyMachine(customOAuth2User.getUserId(), request, image, machineId));
    }
}
