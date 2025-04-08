package org.helloworld.gymmate.domain.user.member.controller;

import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.user.member.dto.MemberCheckResponse;
import org.helloworld.gymmate.domain.user.member.dto.MemberRequest;
import org.helloworld.gymmate.domain.user.member.dto.MemberResponse;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "트레이너 정보 API", description = "트레이너 or 헬스장 운영자 마이페이지, 트레이너 목록 등")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "일반 회원 회원가입", description = "일반 로그인 선택했을 때 회원가입")
    @PostMapping
    public ResponseEntity<Long> registerAdditionalInfo(
        @AuthenticationPrincipal CustomOAuth2User oAuth2User,
        @Valid @RequestBody MemberRequest request
    ) {
        Long memberId = memberService.registerMember(oAuth2User.getUserId(), request);
        return ResponseEntity.ok(memberId);
    }

    @Operation(summary = "일반 회원 마이페이지 정보 수정", description = "일반 회원이 마이페이지에서 자신의 개인 정보 수정")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping
    public ResponseEntity<Long> modify(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
        @RequestPart("memberData") @Valid MemberRequest request,
        @RequestPart(value = "image", required = false) @ValidImageFile MultipartFile profile
    ) {
        return ResponseEntity.ok()
            .body(memberService.modifyMember(
                //매개변수 : member id, memberModifyRequest객체, profile
                oAuth2User.getUserId(), request, profile)

            );
    }

    @Operation(summary = "일반 회원 마이페이지 정보 조회", description = "일반 회원이 마이페이지로 이동했을 때 띄워주는 정보")
    @GetMapping
    public ResponseEntity<MemberResponse> getMyInfo(
        @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        MemberResponse memberResponse = memberService.getMember(oAuth2User.getUserId());

        return ResponseEntity.ok(memberResponse);
    }

    @Operation(summary = "일반 회원 계정 삭제", description = "일반 회원이 자신의 계정을 삭제")
    @DeleteMapping
    public ResponseEntity<Long> deleteMember(
        @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        memberService.deleteMember(oAuth2User.getUserId());

        return ResponseEntity.ok(oAuth2User.getUserId());
    }

    @Operation(summary = "일반 or 트레이너 체크", description = "일반 회원인지 트레이너 회원(직원, 사장)인지 반환, 메인페이지 로직에 사용")
    @GetMapping("/check")
    public ResponseEntity<MemberCheckResponse> check(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        return ResponseEntity.ok()
            .body(memberService.checkUserType(
                    memberService.findByUserId(oAuth2User.getUserId())
                )
            );
    }

}
