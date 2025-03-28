package org.helloworld.gymmate.security.authentication;

import org.helloworld.gymmate.domain.user.member.service.GymmateUserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymmateUserDetailsService implements UserDetailsService {

	private final GymmateUserService gymmateUserService;

	@Override
	public GymmateUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new GymmateUserDetails(gymmateUserService.findByNickName(username));
	}

	public GymmateUserDetails loadUserByUserId(Long userId) {
		return loadUserByUsername(gymmateUserService.findNickNameByUserId(userId));
	}
}