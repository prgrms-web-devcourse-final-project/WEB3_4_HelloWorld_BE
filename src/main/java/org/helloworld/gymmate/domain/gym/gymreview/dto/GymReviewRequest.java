package org.helloworld.gymmate.domain.gym.gymreview.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GymReviewRequest(
    @Min(0) @Max(5) double score,
    String content,
    @Min(1) long gymId
) {
}
