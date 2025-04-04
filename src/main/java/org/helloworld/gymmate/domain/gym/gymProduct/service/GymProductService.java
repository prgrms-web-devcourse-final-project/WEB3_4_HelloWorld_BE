package org.helloworld.gymmate.domain.gym.gymProduct.service;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductDeleteRequest;
import org.helloworld.gymmate.domain.gym.gymProduct.dto.GymProductRequest;
import org.helloworld.gymmate.domain.gym.gymProduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymProduct.mapper.GymProductMapper;
import org.helloworld.gymmate.domain.gym.gymProduct.repository.GymProductRepository;
import org.helloworld.gymmate.domain.gym.partnerGym.dto.request.GymInfoRequest;
import org.helloworld.gymmate.domain.gym.partnerGym.entity.PartnerGym;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GymProductService {
	private final GymProductRepository gymProductRepository;

	// 헬스장 이용권 정보 업데이트
	public void updateGymProducts(GymInfoRequest gymInfoRequest, PartnerGym partnerGym) {
		createGymProducts(gymInfoRequest.gymProductRequest(), partnerGym);
		deleteGymProducts(gymInfoRequest.deleteGymProductRequest(), partnerGym);
	}

	// 헬스장 이용권 정보 생성
	public void createGymProducts(List<GymProductRequest> requests, PartnerGym partnerGym) {
		List<GymProduct> gymProducts = requests.stream()
			.map(request -> GymProductMapper.toEntity(request, partnerGym))
			.toList();
		gymProductRepository.saveAll(gymProducts);
	}

	// 헬스장 이용권 정보 삭제
	public void deleteGymProducts(List<GymProductDeleteRequest> deleteRequests, PartnerGym partnerGym) {
		for (GymProductDeleteRequest request : deleteRequests) {
			Long deleteId = request.gymProductDeleteId();

			gymProductRepository.findById(deleteId)
				.filter(product -> product.getPartnerGym().equals(partnerGym))
				.ifPresent(gymProductRepository::delete);
		}
	}

}
