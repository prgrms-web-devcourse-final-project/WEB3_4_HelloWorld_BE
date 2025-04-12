package org.helloworld.gymmate.domain.user.trainer.trainerreview.controller;

import java.util.List;

import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.request.TrainerReviewCreateRequest;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.request.TrainerReviewModifyRequest;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.service.TrainerReviewService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@Tag(name = "트레이너 리뷰 API", description = "트레이너 리뷰 등록 수정 삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("/trainerreview")
public class TrainerReviewController {

    private final TrainerReviewService trainerReviewService;

    @Operation(summary = "[일반 회원] 트레이너 리뷰 등록")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createTrainerReview(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestPart("trainerReviewData") @Valid TrainerReviewCreateRequest request,
        @RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(trainerReviewService.createTrainerReview(request, images, customOAuth2User.getUserId()));
    }

    @Operation(summary = "[일반 회원] 트래이너 리뷰 수정")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping(value = "/{trainerReviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> modifyTrainerReview(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestPart("trainerReviewData") @Valid TrainerReviewModifyRequest request,
        @RequestPart(value = "images", required = false) @ValidImageFile List<MultipartFile> images,
        @PathVariable long trainerReviewId
    ) {
        return ResponseEntity.ok()
            .body(trainerReviewService.modifyTrainerReview(request, images, trainerReviewId,
                customOAuth2User.getUserId()));
    }

    @Operation(summary = "[일반 회원] 트레이너 리뷰 삭제")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping("/{trainerReviewId}")
    public ResponseEntity<Void> deleteTrainerReview(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long trainerReviewId
    ) {
        trainerReviewService.deleteTrainerReview(trainerReviewId, customOAuth2User.getUserId());
        return ResponseEntity.ok().build();
    }

}
