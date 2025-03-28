package org.helloworld.gymmate.security.service;

import org.helloworld.gymmate.security.model.GymmateUserDetails;
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