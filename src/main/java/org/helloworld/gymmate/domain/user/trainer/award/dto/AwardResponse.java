package org.helloworld.gymmate.domain.user.trainer.award.dto;

public record AwardResponse(
    long awardId,
    String awardYear,
    String awardName,
    String awardInfo
) {
}