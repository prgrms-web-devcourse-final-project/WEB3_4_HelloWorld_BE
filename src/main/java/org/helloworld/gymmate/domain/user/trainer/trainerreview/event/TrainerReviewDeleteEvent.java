package org.helloworld.gymmate.domain.user.trainer.trainerreview.event;

import java.util.List;

import org.helloworld.gymmate.domain.user.trainer.trainerreview.entity.TrainerReviewImage;

public record TrainerReviewDeleteEvent(
    List<TrainerReviewImage> images
) {
}
