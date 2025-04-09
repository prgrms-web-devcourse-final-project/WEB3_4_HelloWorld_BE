package org.helloworld.gymmate.domain.user.trainer.award.service;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.user.trainer.award.dto.AwardRequest;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.award.mapper.AwardMapper;
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

    @Transactional
    public void deleteAward(Long trainerId, Long awardId) {
        Award award = findById(awardId);
        if (!award.getTrainerId().equals(trainerId)) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }
        awardRepository.delete(award);
    }

    public Award findById(Long awardId) {
        return awardRepository.findById(awardId).orElseThrow(
            () -> new BusinessException(ErrorCode.AWARD_NOT_FOUND)
        );
    }

}
