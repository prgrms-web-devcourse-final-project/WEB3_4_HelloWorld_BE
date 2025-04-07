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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/member")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	//회원 추가정보 등록
	@PostMapping
	public ResponseEntity<Long> registerAdditionalInfo(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid MemberRequest request
	) {
		Long memberId = memberService.registerMember(oAuth2User.getUserId(), request);
		return ResponseEntity.ok(memberId);
	}

	//멤버 정보 수정
	// 직원 및 사장 개인정보 수정
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

	//회원 정보 조회
	@GetMapping
	public ResponseEntity<MemberResponse> getMyInfo(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		MemberResponse memberResponse = memberService.getMember(oAuth2User.getUserId());

		return ResponseEntity.ok(memberResponse);
	}

	//회원 삭제
	@DeleteMapping
	public ResponseEntity<Long> deleteMember(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		memberService.deleteMember(oAuth2User.getUserId());

		return ResponseEntity.ok(oAuth2User.getUserId());
	}

	//마이페이지 정보
	@GetMapping("/check")
	public ResponseEntity<MemberCheckResponse> check(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
		return ResponseEntity.ok()
			.body(memberService.checkUserType(
					memberService.findByUserId(oAuth2User.getUserId())
				)
			);
	}

}
