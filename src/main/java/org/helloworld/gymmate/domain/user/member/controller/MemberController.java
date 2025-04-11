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

@Tag(name = "일반 회원 정보 API", description = "일반 회원 정보에 대한 등록, 수정, 삭제, 일반 목록 및 검색 목록 조회")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @Operation(summary = "[정보 등록 전 일반 회원] 일반 회원 회원가입", description = "요청한 일반 회원 자신의 정보를 최초로 등록")
    @PostMapping
    public ResponseEntity<Long> registerAdditionalInfo(
        @AuthenticationPrincipal CustomOAuth2User oAuth2User,
        @Valid @RequestBody MemberRequest request
    ) {
        Long memberId = memberService.registerMemberInfo(oAuth2User.getUserId(), request);
        return ResponseEntity.ok().body(memberId);
    }

    @Operation(summary = "[일반 회원] 일반 회원 정보 수정", description = "요청한 일반 회원 자신의 개인 정보를 수정")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping
    public ResponseEntity<Long> modify(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
        @RequestPart("memberData") @Valid MemberRequest request,
        @RequestPart(value = "image", required = false) @ValidImageFile MultipartFile profile
    ) {
        Long memberId = memberService.modifyMemberInfo(oAuth2User.getUserId(), request, profile);
        return ResponseEntity.ok().body(memberId);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @Operation(summary = "[일반 회원] 일반 회원 계정 삭제", description = "요청한 일반 회원 자신의 계정을 삭제")
    @DeleteMapping
    public ResponseEntity<Long> deleteMember(
        @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        Long memberId = memberService.deleteMember(oAuth2User.getUserId());
        return ResponseEntity.ok().body(memberId);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @Operation(summary = "[일반 회원] 일반 회원 정보 조회", description = "일요청한 일반 회원 자신의 개인 정보를 조회")
    @GetMapping
    public ResponseEntity<MemberResponse> getMyInfo(
        @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        MemberResponse response = memberService.getMemberInfo(oAuth2User.getUserId());
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @Operation(summary = "[로그인한 사용자] 일반 or 트레이너 체크", description = "요청한 사용자가 일반 회원인지 트레이너 회원(직원, 사장 구분 없음)인지 반환")
    @GetMapping("/check")
    public ResponseEntity<MemberCheckResponse> check(
        @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        MemberCheckResponse response = memberService.checkUserType(oAuth2User.getUserId());
        return ResponseEntity.ok().body(response);
    }
}
