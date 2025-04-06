package org.helloworld.gymmate.domain.gym.gymproduct.mapper;

import org.helloworld.gymmate.domain.gym.gymproduct.dto.GymProductRequest;
import org.helloworld.gymmate.domain.gym.gymproduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;

import jakarta.validation.Valid;

public class GymProductMapper {
	public static GymProduct toEntity(@Valid GymProductRequest request, PartnerGym partnerGym) {
		return GymProduct.builder()
			.gymProductName(request.gymProductName())
			.gymProductFee(request.gymProductFee())
			.gymProductMonth(request.gymProductMonth())
			.onSale(true)
			.partnerGym(partnerGym)
			.build();
	}
}
