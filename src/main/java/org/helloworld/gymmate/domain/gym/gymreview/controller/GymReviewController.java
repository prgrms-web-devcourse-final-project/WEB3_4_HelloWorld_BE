package org.helloworld.gymmate.domain.gym.gymreview.controller;

import java.util.List;

import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.gym.gymreview.dto.GymReviewModifyRequest;
import org.helloworld.gymmate.domain.gym.gymreview.dto.GymReviewRequest;
import org.helloworld.gymmate.domain.gym.gymreview.service.GymReviewService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import lombok.extern.slf4j.Slf4j;

@Tag(name = "헬스장 리뷰 API", description = "헬스장 리뷰 등록 수정 삭제")
@RestController
@RequestMapping("/gymreview")
@RequiredArgsConstructor
@Slf4j
public class GymReviewController {
    private final GymReviewService gymReviewService;

    @Operation(summary = "[일반 회원] 헬스장 리뷰 등록")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createGymReview(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestPart("gymReviewData") @Valid GymReviewRequest request,
        @RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(gymReviewService.createGymReview(request, images, customOAuth2User.getUserId()));
    }

    @Operation(summary = "[일반 회원] 헬스장 리뷰 수정")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping(value = "/{gymReviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> modifyGymReview(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestPart("gymReviewData") @Valid GymReviewModifyRequest request,
        @RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images,
        @PathVariable long gymReviewId
    ) {
        return ResponseEntity.ok()
            .body(gymReviewService.modifyGymReview(request, images, gymReviewId, customOAuth2User.getUserId()));
    }

    @Operation(summary = "[일반 회원] 헬스장 리뷰 삭제")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping(value = "/{gymReviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> deleteGymReview(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable long gymReviewId
    ) {
        gymReviewService.deleteGymReview(gymReviewId, customOAuth2User.getUserId());
        return ResponseEntity.ok().build();
    }

}
