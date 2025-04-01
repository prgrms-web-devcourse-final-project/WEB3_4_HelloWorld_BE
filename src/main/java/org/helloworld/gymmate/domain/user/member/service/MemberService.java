package org.helloworld.gymmate.domain.user.member.service;

import java.util.Optional;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.user.member.dto.MemberRequest;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.mapper.MemberMapper;
import org.helloworld.gymmate.domain.user.member.repository.MemberRepository;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.helloworld.gymmate.security.oauth.repository.OauthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final OauthRepository oauthRepository;
	private final EntityManager entityManager;

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
	public Long registerInfoMember(Member member, MemberRequest request) {
		member.registerMemberInfo(request);
		Member savedMember = memberRepository.save(member);
		return savedMember.getMemberId();
	}

	//추가정보 등록 여부 확인
	@Transactional(readOnly = true)
	public boolean check(Member member) {
		return member.getAdditionalInfoCompleted();
	}

	@Transactional(readOnly = true)
	public Optional<Long> getMemberIdByOauth(String providerId) {
		return oauthRepository.findByProviderId(providerId)
			.flatMap(oauth -> memberRepository.findByOauth(oauth)
				.map(Member::getMemberId));
	}

	public Member findByUserId(Long userId) {
		return memberRepository.findByMemberId(userId).orElseThrow(() -> new BusinessException(
			ErrorCode.USER_NOT_FOUND));
	}
}
