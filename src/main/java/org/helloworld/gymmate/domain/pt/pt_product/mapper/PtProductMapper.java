package org.helloworld.gymmate.domain.pt.pt_product.mapper;

import org.helloworld.gymmate.domain.pt.pt_product.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProductImage;

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
}
