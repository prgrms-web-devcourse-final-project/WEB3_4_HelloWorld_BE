package org.helloworld.gymmate.domain.gym.gymProduct.mapper;

import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductRequest;
import org.helloworld.gymmate.domain.gym.gymProduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.partnerGym.entity.PartnerGym;

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
