package org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.request;

import jakarta.validation.constraints.Min;

public record TrainerReviewCreateRequest(
    @Min(0) double score,
    String content,
    @Min(1) Long trainerId
) {
}
