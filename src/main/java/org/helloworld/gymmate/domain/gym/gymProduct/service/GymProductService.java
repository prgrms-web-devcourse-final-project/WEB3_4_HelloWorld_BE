package org.helloworld.gymmate.domain.gym.gymProduct.service;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gymInfo.entity.PartnerGym;
import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductRequest;
import org.helloworld.gymmate.domain.gym.gymProduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymProduct.mapper.GymProductMapper;
import org.helloworld.gymmate.domain.gym.gymProduct.repository.GymProductRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymProductService {

	private final GymProductRepository gymProductRepository;

	public void registerGymProducts(List<GymProductRequest> requests, PartnerGym partnerGym) {
		List<GymProduct> gymProducts = requests.stream()
			.map(request -> GymProductMapper.toEntity(request, partnerGym))
			.toList();

		gymProductRepository.saveAll(gymProducts);
	}

}
