package org.helloworld.gymmate.domain.gym.gymProduct.service;

import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
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
		// 생성 or 수정 호출
		List<GymProductRequest> gymProductRequests = gymInfoRequest.gymProductRequest();
		for (GymProductRequest request : gymProductRequests) {
			if (request.gymProductId() == null) {
				createGymProducts(request, partnerGym);
			} else {
				modifyGymProducts(request);
			}
		}
		// 삭제 호출
		deleteGymProducts(gymInfoRequest.gymProductDeleteIds(), partnerGym);
	}

	// 헬스장 이용권 정보 생성
	public void createGymProducts(GymProductRequest request, PartnerGym partnerGym) {
		gymProductRepository.save(GymProductMapper.toEntity(request, partnerGym));
	}

	// 헬스장 이용권 정보 수정
	public void modifyGymProducts(GymProductRequest request) {
		assert request.gymProductId() != null; // 명시적으로 null 이 아닌 것을 표시
		GymProduct existingProduct = gymProductRepository.findById(request.gymProductId())
			.orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_GYM_NOT_FOUND));
		existingProduct.update(request);
	}

	// 헬스장 이용권 정보 삭제
	public void deleteGymProducts(List<Long> deleteIds, PartnerGym partnerGym) {
		for (Long deleteId : deleteIds) {
			gymProductRepository.findById(deleteId)
				.filter(product -> product.getPartnerGym().equals(partnerGym))
				.ifPresent(gymProductRepository::delete);
		}
	}

}
