package org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.response;

import java.util.List;

public record TrainerReviewResponse(
    Long trainerReviewId,
    Double score,
    String content,
    List<TrainerReviewImageResponse> imageUrls
) {
    public record TrainerReviewImageResponse(
        Long trainerReviewImageId,
        String imageUrl
    ) {
    }
}
