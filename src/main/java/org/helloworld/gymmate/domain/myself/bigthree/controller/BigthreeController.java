package org.helloworld.gymmate.domain.myself.bigthree.controller;

import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeCreateRequest;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeRequest;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeStatsResponse;
import org.helloworld.gymmate.domain.myself.bigthree.service.BigthreeService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "3대 측정", description = "3대 측정 기록(Bench, Deadlift, Squat)에 대한 API")
@RestController
@RequestMapping("/bigthree")
@RequiredArgsConstructor
public class BigthreeController {
    private final BigthreeService bigthreeService;

    @Operation(summary = "[일반 회원] 3대 측정 등록", description = "요청한 회원의 벤치프레스, 데드리프트, 스쿼트 기록을 등록")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<Long> createBigthree(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @Valid @RequestBody BigthreeCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(bigthreeService.createBigthree(request, customOAuth2User.getUserId()));
    }

    @Operation(summary = "[일반 회원] 3대 측정 삭제", description = "선택한 3대 측정 기록을 삭제")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping(value = "/{bigthreeId}")
    public ResponseEntity<Void> deleteBigthree(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long bigthreeId) {

        bigthreeService.deleteBigthree(bigthreeId, customOAuth2User.getUserId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "[일반 회원] 3대 측정 수정", description = "선택한 3대 측정 기록을 수정")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping(value = "/{bigthreeId}")
    public ResponseEntity<Long> modifyBigthree(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long bigthreeId,
        @Valid @RequestBody BigthreeRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(
            bigthreeService.modifyBigthree(bigthreeId, request, customOAuth2User.getUserId()));
    }

    @Operation(summary = "[일반 회원] 3대 측정 통계 조회", description = "요청한 회원의 3대 측정 기록 통계와 사이트 전체 일반 회원들의 3대 기록 통계 조회")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping(value = "/status")
    public ResponseEntity<BigthreeStatsResponse> getBigthreeStats(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseEntity.ok(bigthreeService.getBigthreeStats(customOAuth2User.getUserId()));
    }
}
