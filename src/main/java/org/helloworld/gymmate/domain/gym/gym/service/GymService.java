package org.helloworld.gymmate.domain.gym.gym.service;

import org.helloworld.gymmate.domain.gym.gym.dto.GymCreateRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.GymResponse;
import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.gym.mapper.GymMapper;
import org.helloworld.gymmate.domain.gym.gym.repository.GymRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymService {

	private final GymRepository gymRepository;

	@Transactional
	public GymResponse createGym(GymCreateRequest request) {
		Gym gym = GymMapper.toEntity(request);
		Gym saved = gymRepository.save(gym);
		return GymMapper.toResponse(saved);
	}
}
