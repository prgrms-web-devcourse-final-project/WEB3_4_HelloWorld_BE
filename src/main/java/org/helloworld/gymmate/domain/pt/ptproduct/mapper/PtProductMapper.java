package org.helloworld.gymmate.domain.pt.ptproduct.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.domain.gym.gyminfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gyminfo.entity.GymImage;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductResponse;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductsResponse;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.TrainersPtProductsResponse;
import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProductImage;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;

public class PtProductMapper {
    public static PtProduct toEntity(PtProductCreateRequest request, Long trainerId) {
        return PtProduct.builder()
            .ptProductName(request.ptProductName())
            .info(request.info())
            .ptProductFee(request.ptProductFee())
            .trainerId(trainerId)
            .build();
    }

    public static PtProductImage toEntity(String fileUrl, PtProduct ptProduct) {
        return PtProductImage.builder()
            .url(fileUrl)
            .ptProduct(ptProduct)
            .build();
    }

    public static PtProductsResponse.PtTrainerResponse toDto(Trainer trainer) {
        return new PtProductsResponse.PtTrainerResponse(
            trainer.getTrainerId(),
            trainer.getTrainerName(),
            trainer.getGenderType().toString(),
            trainer.getProfileUrl(),
            trainer.getScore()
        );
    }

    public static PtProductsResponse toDto(PtProduct ptProduct,
        Map<Long, PtProductsResponse.PtTrainerResponse> trainerMap) {
        return new PtProductsResponse(
            ptProduct.getPtProductId(),
            ptProduct.getPtProductName(),
            trainerMap.get(ptProduct.getTrainerId()), // 트레이너 정보 매핑
            ptProduct.getInfo(),
            ptProduct.getPtProductFee()
        );
    }

    public static PtProductResponse toDto(PtProduct ptProduct, Trainer trainer, List<Award> awards, Gym gym) {
        return new PtProductResponse(
            ptProduct.getPtProductId(),
            ptProduct.getPtProductName(),
            new PtProductResponse.Trainer(
                trainer.getTrainerId(),
                trainer.getTrainerName(),
                trainer.getGenderType().toString(),
                trainer.getProfileUrl(),
                trainer.getPhoneNumber(),
                trainer.getEmail(),
                trainer.getScore(),
                awards != null ? awards.stream()
                    .map(a -> new PtProductResponse.Award(a.getAwardYear(), a.getAwardName(), a.getAwardInfo()))
                    .toList() : Collections.emptyList()
            ),
            ptProduct.getInfo(),
            ptProduct.getPtProductFee(),
            ptProduct.getPtProductImages() != null ?
                ptProduct.getPtProductImages().stream().map(PtProductImage::getUrl).toList() : Collections.emptyList(),
            new PtProductResponse.Gym(
                gym.getGymName(),
                gym.getAddress(),
                gym.getLocation().getX(),
                gym.getLocation().getY(),
                gym.getStartTime(),
                gym.getEndTime(),
                gym.getAvgScore(),
                gym.getImages() != null ?
                    gym.getImages().stream().map(GymImage::getUrl).toList() : Collections.emptyList()
            )
        );
    }

    public static TrainersPtProductsResponse toDto(
        List<PtProduct> ptProducts, Trainer trainer, List<Award> awards, Gym gym) {

        TrainersPtProductsResponse.Trainer trainerDto = new TrainersPtProductsResponse.Trainer(
            trainer.getTrainerId(),
            trainer.getTrainerName(),
            trainer.getGenderType().toString(),
            trainer.getProfileUrl(),
            trainer.getPhoneNumber(),
            trainer.getEmail(),
            trainer.getScore(),
            awards != null ? awards.stream()
                .map(a -> new TrainersPtProductsResponse.Award(a.getAwardYear(), a.getAwardName(), a.getAwardInfo()))
                .toList() : Collections.emptyList()
        );

        TrainersPtProductsResponse.Gym gymDto = new TrainersPtProductsResponse.Gym(
            gym.getGymId(),
            gym.getGymName(),
            gym.getAddress(),
            gym.getLocation().getX(),
            gym.getLocation().getY(),
            gym.getStartTime(),
            gym.getEndTime(),
            gym.getAvgScore(),
            gym.getImages() != null ?
                gym.getImages().stream().map(GymImage::getUrl).toList() : Collections.emptyList()
        );

        List<TrainersPtProductsResponse.PtProduct> ptProductDtos = ptProducts.stream()
            .map(pt -> new TrainersPtProductsResponse.PtProduct(
                pt.getPtProductId(),
                pt.getPtProductName(),
                pt.getInfo(),
                pt.getPtProductFee(),
                pt.getPtProductImages() != null ?
                    pt.getPtProductImages().stream().map(PtProductImage::getUrl).toList() : Collections.emptyList()
            ))
            .toList();

        return new TrainersPtProductsResponse(trainerDto, gymDto, ptProductDtos);
    }
}
