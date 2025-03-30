package org.helloworld.gymmate.security.authentication;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.repository.MemberRepository;
import org.helloworld.gymmate.domain.user.service.GymmateUserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymmateUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public GymmateUserDetails loadUserByUsername(String username){
		Member member = memberRepository.findByMemberName(username)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND)
			);

		return new GymmateUserDetails(member);
	}

	public GymmateUserDetails loadUserByUserId(Long userId) {
		return loadUserByUsername(
			memberRepository.findMemberNameByMemberId(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND))
		);
	}
}