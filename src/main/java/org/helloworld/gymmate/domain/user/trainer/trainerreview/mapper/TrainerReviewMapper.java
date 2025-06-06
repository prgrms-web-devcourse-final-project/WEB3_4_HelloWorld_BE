package org.helloworld.gymmate.domain.user.trainer.trainerreview.mapper;

import java.util.Optional;

import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.request.TrainerReviewCreateRequest;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.dto.response.TrainerReviewResponse;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.entity.TrainerReview;
import org.helloworld.gymmate.domain.user.trainer.trainerreview.entity.TrainerReviewImage;

public class TrainerReviewMapper {
    public static TrainerReview toEntity(TrainerReviewCreateRequest reviewRequest, Trainer trainer, Long memberId) {
        return TrainerReview.builder()
            .score(reviewRequest.score())
            .content(reviewRequest.content())
            .memberId(memberId)
            .trainer(trainer)
            .build();
    }

    public static TrainerReviewImage toImageEntity(String imageUrl, TrainerReview trainerReview) {
        return TrainerReviewImage.builder()
            .url(imageUrl)
            .trainerReview(trainerReview)
            .build();
    }

    public static TrainerReviewResponse toResponse(TrainerReview trainerReview, Member member) {
        return new TrainerReviewResponse(
            trainerReview.getTrainerReviewId(),
            trainerReview.getScore(),
            trainerReview.getContent(),
            trainerReview.getCreatedAt().toString(),
            Optional.ofNullable(trainerReview.getModifiedAt()).map(Object::toString).orElse(null),
            trainerReview.getImages().stream().map(TrainerReviewMapper::toImageResponse).toList(),
            member.getMemberName(),
            member.getProfileUrl(),
            member.getLevel()
        );
    }

    public static TrainerReviewResponse.TrainerReviewImageResponse toImageResponse(TrainerReviewImage image) {
        return new TrainerReviewResponse.TrainerReviewImageResponse(
            image.getTrainerReviewImageId(),
            image.getUrl()
        );
    }
}
