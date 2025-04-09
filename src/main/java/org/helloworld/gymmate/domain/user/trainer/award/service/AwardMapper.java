package org.helloworld.gymmate.domain.user.trainer.award.service;

import org.helloworld.gymmate.domain.user.trainer.award.dto.AwardRequest;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;

public class AwardMapper {
    public static Award toEntity(long trainerId, AwardRequest awardRequest) {
        return Award.builder()
            .trainerId(trainerId)
            .awardYear(awardRequest.awardYear())
            .awardName(awardRequest.awardName())
            .awardInfo(awardRequest.awardInfo())
            .build();
    }
}
