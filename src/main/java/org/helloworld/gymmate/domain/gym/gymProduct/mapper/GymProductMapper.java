package org.helloworld.gymmate.domain.gym.gymProduct.mapper;

import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductRequest;
import org.helloworld.gymmate.domain.gym.gymProduct.entity.GymProduct;

import jakarta.validation.Valid;

public class GymProductMapper {
	public static GymProduct toEntityWithoutPartnerGym(@Valid GymProductRequest request) {
		return GymProduct.builder()
			.gymProductFee(request.gymProductFee())
			.gymProductMonth(request.gymProductMonth())
			.onSale(true)
			.build();
	}
}
