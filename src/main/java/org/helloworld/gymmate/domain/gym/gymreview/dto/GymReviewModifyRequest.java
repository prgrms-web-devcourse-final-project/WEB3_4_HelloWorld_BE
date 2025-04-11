package org.helloworld.gymmate.domain.gym.gymreview.dto;

import java.util.List;

import jakarta.validation.constraints.Min;

public record GymReviewModifyRequest(
    @Min(0) double score,
    String content,
    List<String> deleteImageUrls
) {
}
