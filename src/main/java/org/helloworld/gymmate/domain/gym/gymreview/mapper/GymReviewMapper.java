package org.helloworld.gymmate.domain.gym.gymreview.mapper;

import java.util.Objects;
import java.util.Optional;

import org.helloworld.gymmate.domain.gym.gymreview.dto.GymReviewImageResponse;
import org.helloworld.gymmate.domain.gym.gymreview.dto.GymReviewRequest;
import org.helloworld.gymmate.domain.gym.gymreview.dto.GymReviewResponse;
import org.helloworld.gymmate.domain.gym.gymreview.entity.GymReview;
import org.helloworld.gymmate.domain.gym.gymreview.entity.GymReviewImage;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;

public class GymReviewMapper {
    public static GymReview toEntity(GymReviewRequest request, Long memberId, PartnerGym partnerGym) {
        return GymReview.builder()
            .score(request.score())
            .content(request.content())
            .memberId(memberId)
            .partnerGym(partnerGym)
            .build();
    }

    public static GymReviewImage toImageEntity(String fileUrl, GymReview gymReview) {
        return GymReviewImage.builder()
            .url(fileUrl)
            .gymReview(gymReview)
            .build();
    }

    public static GymReviewResponse toResponse(GymReview gymReview) {
        return new GymReviewResponse(
            gymReview.getGymReviewId(),
            gymReview.getScore(),
            gymReview.getContent(),
            gymReview.getCreatedAt().toString(),
            Optional.ofNullable(gymReview.getModifiedAt()).map(Objects::toString).orElse(null),
            gymReview.getImages().stream()
                .map(GymReviewMapper::toImageResponse).toList()
        );
    }

    public static GymReviewImageResponse toImageResponse(GymReviewImage gymReviewImage) {
        return new GymReviewImageResponse(
            gymReviewImage.getGymReviewImageId(),
            gymReviewImage.getUrl()
        );
    }
}