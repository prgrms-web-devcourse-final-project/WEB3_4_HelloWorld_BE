package org.helloworld.gymmate.domain.user.trainer.award.service;

import org.helloworld.gymmate.domain.user.trainer.award.dto.AwardRequest;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.award.repository.AwardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwardService {
    private final AwardRepository awardRepository;

    @Transactional
    public Long createAward(Long trainerId, @Valid AwardRequest awardRequest) {
        Award award = awardRepository.save(AwardMapper.toEntity(trainerId, awardRequest));
        return award.getAwardId();
    }
}
