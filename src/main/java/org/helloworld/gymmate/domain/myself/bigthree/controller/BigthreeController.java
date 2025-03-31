package org.helloworld.gymmate.domain.myself.bigthree.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeCreateRequest;
import org.helloworld.gymmate.domain.myself.bigthree.service.BigthreeService;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bigthree")
@RequiredArgsConstructor
public class BigthreeController {
    private BigthreeService bigthreeService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createBigthree(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody BigthreeCreateRequest request) {

        Member member = null; //TODO: memberService.findByUserId(customOAuth2User.getUserId());
        long bigthreeId = bigthreeService.createBigthree(request, member);

        return ResponseEntity.ok(
                Map.of("bigthreeId", bigthreeId));
    }

    @DeleteMapping(value = "/{bigthreeId}")
    public ResponseEntity<Map<String, Long>> deleteBigthree(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long bigthreeId) {

        Member member = null; //TODO: memberService.findByUserId(customOAuth2User.getUserId());
        bigthreeService.deleteBigthree(bigthreeId, member);

        return ResponseEntity.ok().build();
    }

}
