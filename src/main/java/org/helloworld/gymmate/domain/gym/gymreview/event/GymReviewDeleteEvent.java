package org.helloworld.gymmate.domain.gym.gymreview.event;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gymreview.entity.GymReviewImage;

public record GymReviewDeleteEvent(
    List<GymReviewImage> images
) {
}
