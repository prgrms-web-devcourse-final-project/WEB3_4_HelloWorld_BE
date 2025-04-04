package org.helloworld.gymmate.domain.pt.ptProduct.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.GymImage;
import org.helloworld.gymmate.domain.pt.ptProduct.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.ptProduct.dto.PtProductResponse;
import org.helloworld.gymmate.domain.pt.ptProduct.dto.PtProductsResponse;
import org.helloworld.gymmate.domain.pt.ptProduct.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.ptProduct.entity.PtProductImage;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;

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
			trainer.getProfile(),
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
				trainer.getProfile(),
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
}
