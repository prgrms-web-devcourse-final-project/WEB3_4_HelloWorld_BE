package org.helloworld.gymmate.domain.user.trainer.award.dto;

import jakarta.validation.constraints.NotBlank;

public record AwardRequest(
	@NotBlank String awardYear,
	@NotBlank String awardName,
	@NotBlank String awardInfo
) {
}