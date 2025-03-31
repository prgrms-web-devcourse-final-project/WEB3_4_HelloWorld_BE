package org.helloworld.gymmate.domain.user.member.service;

import org.helloworld.gymmate.domain.user.enums.SocialProviderType;
import org.helloworld.gymmate.domain.user.member.dto.MemberRequest;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.mapper.MemberMapper;
import org.helloworld.gymmate.domain.user.member.repository.MemberRepository;
import org.helloworld.gymmate.security.oauth.repository.OauthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final OauthRepository oauthRepository;

	@Transactional
	public Long createMember(SocialProviderType socialProviderType, String socialProviderId,
		MemberRequest memberRequest) {
		SocialProvider socialProvider = SocialProviderMapper.toEntity(socialProviderType, socialProviderId);
		socialProviderRepository.save(socialProvider);

		Member member = MemberMapper.toMember(socialProvider, memberRequest);

		return memberRepository.save(member).getMemberId();
	}

}
