package org.helloworld.gymmate.domain.gym.gymreview.dto;

import java.util.List;

public record GymReviewResponse(
    Long gymReviewId,
    Double score,
    String content,
    String createdAt,
    String modifiedAt,
    List<GymReviewImageResponse> images,
    String memberName,
    String memberProfileUrl,
    int memberLevel
) {
}
