package org.helloworld.gymmate.domain.user.trainer.award.controller;

import org.helloworld.gymmate.domain.user.trainer.award.dto.AwardRequest;
import org.helloworld.gymmate.domain.user.trainer.award.service.AwardService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "트레이너 수상이력 API", description = "트레이너의 수상이력 등록, 삭제, 조회")
@RestController
@RequestMapping("/trainer/award")
@RequiredArgsConstructor
public class AwardController {
    private final AwardService awardService;

    @Operation(summary = "트레이너 수상이력 등록")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Long> createAward(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestBody @Valid AwardRequest awardRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(awardService.createAward(customOAuth2User.getUserId(), awardRequest));
    }

    @Operation(summary = "트레이너 수상이력 삭제")
    @DeleteMapping("/{awardId}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Void> deleteAward(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long awardId
    ) {
        awardService.deleteAward(customOAuth2User.getUserId(), awardId);
        return ResponseEntity.ok().build();
    }

}
