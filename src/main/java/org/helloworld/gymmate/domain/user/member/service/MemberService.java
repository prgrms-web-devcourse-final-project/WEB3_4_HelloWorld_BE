package org.helloworld.gymmate.domain.user.member.service;

import java.util.Optional;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.member.dto.MemberCheckResponse;
import org.helloworld.gymmate.domain.user.member.dto.MemberRequest;
import org.helloworld.gymmate.domain.user.member.dto.MemberResponse;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.mapper.MemberMapper;
import org.helloworld.gymmate.domain.user.member.repository.MemberRepository;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.helloworld.gymmate.security.oauth.repository.OauthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final OauthRepository oauthRepository;
	private final EntityManager entityManager;
	private final FileManager fileManager;

	@Transactional
	public Long createMember(Oauth oauth) {
		if (!entityManager.contains(oauth)) {
			oauth = entityManager.merge(oauth);
		}

		Member member = MemberMapper.toMember(oauth);

		return memberRepository.save(member).getMemberId();
	}

	//추가정보 등록
	@Transactional
	public Long registerMember(Long memberId, MemberRequest request) {
		// 1. 멤버id로 멤버 조회
		Member member = findByUserId(memberId);

		// 2. 멤버 객체 수정
		member.registerMemberInfo(request);

		//3. 저장
		return memberRepository.save(member).getMemberId();
	}

	//추가정보 등록 여부 확인
	@Transactional(readOnly = true)
	public boolean check(Member member) {
		return member.getAdditionalInfoCompleted();
	}

	@Transactional(readOnly = true)
	public Optional<Long> getMemberIdByOauth(String providerId) {
		return oauthRepository.findByProviderIdAndUserType(providerId, UserType.MEMBER)
			.flatMap(oauth -> memberRepository.findByOauth(oauth)
				.map(Member::getMemberId));
	}

	public Member findByUserId(Long userId) {
		return memberRepository.findByMemberId(userId).orElseThrow(() -> new BusinessException(
			ErrorCode.USER_NOT_FOUND));
	}

	// 멤버 정보 수정
	@Transactional
	public Long modifyMember(Long memberId, MemberRequest request, MultipartFile image) {

		//1. memberId로 member 조회
		Member member = findByUserId(memberId);

		//2.1. 기존 이미지 URL 유지
		String imageUrl = member.getProfileUrl();

		//2.2. 새로운 이미지가 있다면,
		if (image != null && !image.isEmpty()) {
			//이전에 저장된 멤버의 프로필 사진 삭제
			fileManager.deleteFile(member.getProfileUrl());

			//새로운 프로필 사진 업로드 & url 저장
			imageUrl = fileManager.uploadFile(image, "member");
		}

		//3. 객체 정보 수정
		member.modifyMemberInfo(request, imageUrl);

		//4.객체 저장
		return memberRepository.save(member).getMemberId();
	}

	@Transactional
	public void deleteMember(Long memberId) {
		log.debug("회원 삭제 시작: memberId={}", memberId);

		//멤버 객체를 찾아서 파일 삭제
		Member member = findByUserId(memberId);
		fileManager.deleteFile(member.getProfileUrl());

		//멤버 테이블에서 멤버 삭제
		memberRepository.deleteByMemberId(memberId);
	}

	@Transactional
	public MemberResponse getMember(Long userId) {
		Member member = findByUserId(userId);
		return MemberMapper.toResponseDto(member);
	}

	@Transactional(readOnly = true)
	public MemberCheckResponse checkUserType(Member member) {
		return MemberMapper.toCheckResponse(member);
	}

}
