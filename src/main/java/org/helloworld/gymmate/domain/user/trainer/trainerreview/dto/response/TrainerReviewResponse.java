package org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.response;

import java.util.List;

public record TrainerReviewResponse(
    Long trainerReviewId,
    Double score,
    String content,
    String createdAt,
    String modifiedAt,
    List<TrainerReviewImageResponse> imageUrls
) {
    public record TrainerReviewImageResponse(
        Long trainerReviewImageId,
        String imageUrl
    ) {
    }
}
