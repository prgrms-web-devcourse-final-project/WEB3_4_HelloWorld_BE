package org.helloworld.gymmate.domain.user.trainer.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.domain.gym.gyminfo.dto.response.TrainerDetailResponse;
import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProduct;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerCheckResponse;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerListResponse;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerResponse;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {

    public static Trainer toTrainer(Oauth oauth) {
        return Trainer.builder()
            .oauth(oauth)
            .isOwner(false)
            .isCheck(false)
            .score(0.0)
            .cash(5000L)
            .additionalInfoCompleted(false)
            .build();
    }

    public static TrainerResponse toResponse(Trainer trainer) {
        return new TrainerResponse(trainer.getTrainerId(),
            trainer.getTrainerName(),
            trainer.getBank(),
            trainer.getAccount(),
            trainer.getGenderType().toString(),
            trainer.getProfileUrl(),
            trainer.getPhoneNumber(),
            trainer.getEmail(),
            trainer.getIsOwner(),
            trainer.getCash(),
            trainer.getIntro(),
            trainer.getCareer(),
            trainer.getField());
    }

    public static TrainerCheckResponse toCheckResponse(Trainer trainer) {
        return new TrainerCheckResponse(
            UserType.TRAINER.toString(),
            trainer.getIsOwner()
        );
    }

    public static TrainerListResponse toListResponse(Trainer trainer, Map<Long, List<String>> awardsMap) {
        return new TrainerListResponse(
            trainer.getTrainerId(),
            trainer.getTrainerName(),
            trainer.getProfileUrl(),
            trainer.getScore(),
            trainer.getIntro(),
            trainer.getCareer(),
            trainer.getField(),
            awardsMap.getOrDefault(trainer.getTrainerId(), Collections.emptyList()) // 수상 경력 없으면 빈 리스트
        );
    }

    public TrainerDetailResponse toTrainerDetailResponse(
        Trainer trainer,
        List<Award> awards,
        List<PtProduct> ptProducts
    ) {
        List<TrainerDetailResponse.AwardResponse> awardDtos = awards.stream()
            .map(award -> new TrainerDetailResponse.AwardResponse(
                award.getAwardYear(),
                award.getAwardName(),
                award.getAwardInfo()
            )).toList();

        List<TrainerDetailResponse.PtProductResponse> productDtos = ptProducts.stream()
            .map(p -> new TrainerDetailResponse.PtProductResponse(
                p.getPtProductName(),
                p.getPtProductFee().intValue()
            )).toList();

        return new TrainerDetailResponse(
            trainer.getTrainerName(),
            trainer.getIntro(),
            trainer.getField(),
            trainer.getCareer(),
            trainer.getProfileUrl(),
            awardDtos,
            productDtos
        );
    }
}
