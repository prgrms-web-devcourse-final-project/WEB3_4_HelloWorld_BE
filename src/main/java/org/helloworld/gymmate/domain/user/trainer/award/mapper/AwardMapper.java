package org.helloworld.gymmate.domain.user.trainer.award.mapper;

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
}
