package org.helloworld.gymmate.domain.user.member.service;

import java.util.Optional;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.member.dto.MemberCheckResponse;
import org.helloworld.gymmate.domain.user.member.dto.MemberModifyRequest;
import org.helloworld.gymmate.domain.user.member.dto.MemberRequest;
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
	public Long registerInfoMember(Member member, MemberRequest request, MultipartFile image) {
		String imageUrl = null;

		if (image != null && !image.isEmpty()) {
			// 파일 업로드
			imageUrl = fileManager.uploadFile(image, "member");
		}

		member.registerMemberInfo(request, imageUrl);

		return memberRepository.save(member).getMemberId();
	}

	@Transactional
	public void saveMemberProfile(Member member, MultipartFile image) {

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

	@Transactional
	public void deleteMember(Long memberId) {
		log.debug("회원 삭제 시작: memberId={}", memberId);

		//멤버 객체를 찾아서 파일 삭제
		Member member = findByUserId(memberId);
		fileManager.deleteFile(member.getProfileUrl());

		//멤버 테이블에서 멤버 삭제
		memberRepository.deleteByMemberId(memberId);
		log.debug("회원이 성공적으로 삭제되었습니다. memberId={}", memberId);
	}

	@Transactional(readOnly = true)
	public MemberCheckResponse checkUserType(Member member) {
		return MemberMapper.toCheckResponse(member);
	}

	// 멤버 정보 수정
	@Transactional
	public Long modifyMemberInfo(Member member, MemberModifyRequest request, MultipartFile image) {

		//1. 이전에 저장된 파일 삭제
		fileManager.deleteFile(member.getProfileUrl());

		//2. 새로운 파일 url 저장
		String imageUrl = null;
		if (image != null && !image.isEmpty()) {
			// 파일 업로드
			imageUrl = fileManager.uploadFile(image, "member");
		}

		//3. 객체 정보 수정
		member.modifyMemberInfo(request, imageUrl);

		//4.저장
		return memberRepository.save(member).getMemberId();
	}
}
