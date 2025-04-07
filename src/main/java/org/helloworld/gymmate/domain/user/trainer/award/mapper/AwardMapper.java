package org.helloworld.gymmate.domain.user.trainer.award.mapper;

import org.helloworld.gymmate.domain.user.trainer.award.dto.AwardRequest;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;

public class AwardMapper {
	public static Award toEntity(Long trainerId, AwardRequest awardRequest) {
		return Award.builder()
			.awardYear(awardRequest.awardYear())
			.awardName(awardRequest.awardName())
			.awardInfo(awardRequest.awardInfo())
			.trainerId(trainerId)
			.build();
	}
}
