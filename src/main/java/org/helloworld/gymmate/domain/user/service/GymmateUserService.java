package org.helloworld.gymmate.domain.user.service;

import java.util.Optional;

import org.helloworld.gymmate.domain.user.model.GymmateUser;
import org.springframework.stereotype.Service;

@Service
public class GymmateUserService {
	public Optional<Long> existSocialProvider(String providerId) {
		return null;
	}

	public GymmateUser findByNickName(String username) {
		return null;
	}

	public String findNickNameByUserId(Long userId) {
		return "";
	}
}
