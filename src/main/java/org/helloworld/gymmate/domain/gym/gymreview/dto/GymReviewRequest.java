package org.helloworld.gymmate.domain.gym.gymreview.dto;

import jakarta.validation.constraints.Min;

public record GymReviewRequest(
    @Min(0) double score,
    String content,
    @Min(1) long gymId
) {
}
