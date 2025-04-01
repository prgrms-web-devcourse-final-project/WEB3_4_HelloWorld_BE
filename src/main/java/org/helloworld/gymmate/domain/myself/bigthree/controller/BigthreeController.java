package org.helloworld.gymmate.domain.myself.bigthree.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeCreateRequest;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeRequest;
import org.helloworld.gymmate.domain.myself.bigthree.service.BigthreeService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bigthree")
@RequiredArgsConstructor
public class BigthreeController {
    private final BigthreeService bigthreeService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createBigthree(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody BigthreeCreateRequest request) {

        long bigthreeId = bigthreeService.createBigthree(request, customOAuth2User.getUserId());

        return ResponseEntity.ok(
                Map.of("bigthreeId", bigthreeId));
    }

    @DeleteMapping(value = "/{bigthreeId}")
    public ResponseEntity<Map<String, Long>> deleteBigthree(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long bigthreeId) {

        bigthreeService.deleteBigthree(bigthreeId, customOAuth2User.getUserId());

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{bigthreeId}")
    public ResponseEntity<Map<String, Long>> modifyBigthree(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long bigthreeId,
            @Valid @RequestBody BigthreeRequest request) {

        bigthreeService.modifyBigthree(bigthreeId, request, customOAuth2User.getUserId());

        return ResponseEntity.ok().build();
    }

}
