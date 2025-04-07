package org.helloworld.gymmate.domain.user.trainer.award.service;

import org.helloworld.gymmate.domain.user.trainer.award.dto.AwardRequest;
import org.helloworld.gymmate.domain.user.trainer.award.mapper.AwardMapper;
import org.helloworld.gymmate.domain.user.trainer.award.repository.AwardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwardService {
	private final AwardRepository awardRepository;

	@Transactional
	public Long createAward(Long trainerId, AwardRequest awardRequest) {
		return awardRepository.save(AwardMapper.toEntity(trainerId, awardRequest)).getAwardId();
	}
}
