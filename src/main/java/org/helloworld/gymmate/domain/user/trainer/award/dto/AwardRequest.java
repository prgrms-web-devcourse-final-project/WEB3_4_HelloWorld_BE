package org.helloworld.gymmate.domain.user.trainer.award.dto;

public record AwardRequest(
    String awardYear,
    String awardName,
    String awardInfo
) {
}
