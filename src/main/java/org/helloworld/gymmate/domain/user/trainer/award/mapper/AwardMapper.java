package org.helloworld.gymmate.domain.user.trainer.award.mapper;

import org.helloworld.gymmate.domain.user.trainer.award.dto.AwardRequest;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.award.enums.AwardData;

public class AwardMapper {

    public static Award toEntity(AwardData awardData, Long trainerId) {
        return Award.builder()
            .awardYear(awardData.getAwardYear())
            .awardName(awardData.getAwardName())
            .awardInfo(awardData.getAwardInfo())
            .trainerId(trainerId)
            .build();
    }

    public static Award toEntity(long trainerId, AwardRequest awardRequest) {
        return Award.builder()
            .trainerId(trainerId)
            .awardYear(awardRequest.awardYear())
            .awardName(awardRequest.awardName())
            .awardInfo(awardRequest.awardInfo())
            .build();
    }

}