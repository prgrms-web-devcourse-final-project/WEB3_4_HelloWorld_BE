package org.helloworld.gymmate.domain.gym.gymreview.mapper;

import org.helloworld.gymmate.domain.gym.gymreview.dto.GymReviewRequest;
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
}