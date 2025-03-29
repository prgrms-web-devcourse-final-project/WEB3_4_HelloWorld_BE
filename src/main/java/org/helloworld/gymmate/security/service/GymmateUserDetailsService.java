package org.helloworld.gymmate.security.service;

import java.util.Collections;

import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.helloworld.gymmate.security.model.GymmateUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymmateUserDetailsService implements UserDetailsService {

	private final GymmateUserService gymmateUserService;
	private final TrainerService trainerService;

	@Override
	public GymmateUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		throw new UnsupportedOperationException("username 기반 조회는 지원하지 않습니다. userId와 userType을 사용하세요.");
	}

	// userId와 userType을 기반으로 사용자 정보를 조회하는 메서드
	public GymmateUserDetails loadUserByUserId(Long userId, String userType) {
		userType = userType.toUpperCase();
		if ("TRAINER".equalsIgnoreCase(userType)) {
			var trainer = trainerService.findByUserId(userId);
			if (trainer == null) {
				throw new UsernameNotFoundException("Trainer not found with id: " + userId);
			}
			return new GymmateUserDetails(trainer.getTrainerId(), trainer.getTrainerName(), "TRAINER",
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_TRAINER")));
		} else {
			throw new UsernameNotFoundException("Unknown user type: " + userType);
		}
	}
}