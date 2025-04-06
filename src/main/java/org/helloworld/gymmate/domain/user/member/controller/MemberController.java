package org.helloworld.gymmate.domain.user.member.controller;

import org.helloworld.gymmate.common.validate.custom.ValidImageFile;
import org.helloworld.gymmate.domain.user.member.dto.MemberCheckResponse;
import org.helloworld.gymmate.domain.user.member.dto.MemberModifyRequest;
import org.helloworld.gymmate.domain.user.member.dto.MemberRequest;
import org.helloworld.gymmate.domain.user.member.dto.MemberResponse;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.mapper.MemberMapper;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.helloworld.gymmate.security.oauth.service.CustomOAuth2UserService;
import org.springframework.http.MediaType;
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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/member")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final CustomOAuth2UserService customOAuth2UserService;

	//회원 추가정보 등록
	@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Long> registerAdditionalInfo(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestPart("memberData") @Valid MemberRequest request,
		@RequestPart(value = "image", required = false) @ValidImageFile MultipartFile profile
	) {
		log.debug("회원 추가 정보 등록 요청: userId={}", oAuth2User.getUserId());

		Member member = memberService.findByUserId(oAuth2User.getUserId());
		Long memberId = memberService.registerInfoMember(member, request, profile);
		return ResponseEntity.ok(memberId);
	}

	//회원 정보 조회
	@GetMapping("/me")
	public ResponseEntity<MemberResponse> getMyInfo(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		Member member = memberService.findByUserId(oAuth2User.getUserId());
		MemberResponse memberResponse = MemberMapper.toResponseDto(member);

		return ResponseEntity.ok(memberResponse);
	}

	//회원 삭제
	@DeleteMapping
	public ResponseEntity<Void> deleteMember(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		log.debug("회원 탈퇴 요청: userId={}", oAuth2User.getUserId());

		memberService.deleteMember(oAuth2User.getUserId());

		log.debug("회원 탈퇴 완료: userId={}", oAuth2User.getUserId());

		return ResponseEntity.ok().build();
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

	//멤버 정보 수정
	// 직원 및 사장 개인정보 수정
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@PutMapping
	public ResponseEntity<Long> modify(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestBody MemberModifyRequest request) {
		return ResponseEntity.ok()
			.body(memberService.modifyMemberInfo(
				memberService.findByUserId(customOAuth2User.getUserId()), request) //member 객체, memberModifyRequest객체
			);
	}
}
