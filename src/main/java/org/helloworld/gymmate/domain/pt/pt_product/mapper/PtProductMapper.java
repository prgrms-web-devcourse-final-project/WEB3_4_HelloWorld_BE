package org.helloworld.gymmate.domain.pt.pt_product.mapper;

import java.util.Map;

import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductsResponse;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProductImage;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;

public class PtProductMapper {
	public static PtProduct toEntity(PtProductCreateRequest request, Long trainerId){
		return PtProduct.builder()
			.info(request.info())
			.ptProductFee(request.ptProductFee())
			.trainerId(trainerId)
			.build();
	}

	public static PtProductImage toEntity(String fileUrl, PtProduct ptProduct){
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

	public static PtProductsResponse toDto(PtProduct ptProduct, Map<Long, PtProductsResponse.PtTrainerResponse> trainerMap) {
		return new PtProductsResponse(
			ptProduct.getPtProductId(),
			trainerMap.get(ptProduct.getTrainerId()), // 트레이너 정보 매핑
			ptProduct.getInfo(),
			ptProduct.getPtProductFee()
		);
	}
}
