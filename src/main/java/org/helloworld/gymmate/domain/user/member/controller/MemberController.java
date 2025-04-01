package org.helloworld.gymmate.domain.user.member.controller;

import java.util.Map;

import org.helloworld.gymmate.domain.user.member.dto.MemberRequest;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	/**
	 * 회원 추가 정보 등록
	 */
	@PostMapping("/member/register")
	public ResponseEntity<Map<String, Long>> registerAdditionalInfo(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid @RequestBody MemberRequest request
	) {
		log.info("회원 추가 정보 등록 요청: userId={}", oAuth2User.getUserId());

		Member member = memberService.findByUserId(oAuth2User.getUserId());
		Long memberId = memberService.registerInfoMember(member, request);

		return ResponseEntity.ok(Map.of("memberId", memberId));
	}

	/**
	 * 회원 추가 정보 등록 여부 확인
	 */
	@GetMapping("/check-info")
	public ResponseEntity<Map<String, Boolean>> checkAdditionalInfo(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		Member member = memberService.findByUserId(oAuth2User.getUserId());
		boolean isCompleted = memberService.check(member);

		return ResponseEntity.ok(Map.of("isCompleted", isCompleted));
	}

	/**
	 * 회원 정보 조회
	 */
	@GetMapping("/me")
	public ResponseEntity<Map<String, Member>> getMyInfo(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		Member member = memberService.findByUserId(oAuth2User.getUserId());
		return ResponseEntity.ok(Map.of("member", member));
	}
}