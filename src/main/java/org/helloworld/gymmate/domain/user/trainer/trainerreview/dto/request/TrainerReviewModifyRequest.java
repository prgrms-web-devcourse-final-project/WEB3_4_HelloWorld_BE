package org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.request;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record TrainerReviewModifyRequest(
    @Min(0) @Max(5) double score,
    String content,
    List<String> deleteImageUrls
) {
}
