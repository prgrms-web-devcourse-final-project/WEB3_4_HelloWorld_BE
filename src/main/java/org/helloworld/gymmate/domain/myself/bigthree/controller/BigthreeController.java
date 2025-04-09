package org.helloworld.gymmate.domain.myself.bigthree.controller;

import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeCreateRequest;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeRequest;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeStatsResponse;
import org.helloworld.gymmate.domain.myself.bigthree.service.BigthreeService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bigthree")
@RequiredArgsConstructor
public class BigthreeController {
    private final BigthreeService bigthreeService;

    @PostMapping
    public ResponseEntity<Long> createBigthree(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @Valid @RequestBody BigthreeCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(bigthreeService.createBigthree(request, customOAuth2User.getUserId()));
    }

    @DeleteMapping(value = "/{bigthreeId}")
    public ResponseEntity<Void> deleteBigthree(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long bigthreeId) {

        bigthreeService.deleteBigthree(bigthreeId, customOAuth2User.getUserId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "/{bigthreeId}")
    public ResponseEntity<Long> modifyBigthree(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @PathVariable Long bigthreeId,
        @Valid @RequestBody BigthreeRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(
            bigthreeService.modifyBigthree(bigthreeId, request, customOAuth2User.getUserId()));
    }

    @GetMapping(value = "/status")
    public ResponseEntity<BigthreeStatsResponse> getBigthreeStats(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseEntity.ok(bigthreeService.getBigthreeStats(customOAuth2User.getUserId()));
    }
}
