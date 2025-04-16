package org.helloworld.gymmate.domain.pt.ptproduct.dto;

import java.util.List;

public record TrainersPtProductsResponse(
    Trainer trainer,
    Gym gym,
    List<PtProduct> ptProducts
) {
    public record PtProduct(
        Long ptProductId,
        String productName,
        String ptInfo,
        Long fee,
        List<String> images
    ) {
    }

    public record Trainer(
        Long trainerId,
        String trainerName,
        String gender,
        String profile,
        String contact,
        String email,
        Double trainerScore,
        List<Award> awards
    ) {
    }

    public record Award(
        String year,
        String awardName,
        String info
    ) {
    }

    public record Gym(
        long gymId,
        String gymName,
        String gymAddress,
        Double gymX,
        Double gymY,
        String gymOpen,
        String gymClose,
        Double gymScore,
        List<String> images
    ) {
    }
}