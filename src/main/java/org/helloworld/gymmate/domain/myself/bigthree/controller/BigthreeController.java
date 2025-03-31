package org.helloworld.gymmate.domain.myself.bigthree.controller;

import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.domain.myself.bigthree.service.BigthreeService;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/bigthree")
@RequiredArgsConstructor
public class BigthreeController {
    private BigthreeService bigthreeService;


    @DeleteMapping(value = "/{bigthreeId}")
    public ResponseEntity<Map<String, Long>> deleteBigthree(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long bigthreeId) {

        Member member = null; //TODO: memberService.findByUserId(customOAuth2User.getUserId());
        bigthreeService.deleteBigthree(bigthreeId, member);

        return ResponseEntity.ok().build();
    }
    
}
