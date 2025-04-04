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

	// 헬스장 이용권 정보 등록
	public void registerGymProducts(List<GymProductRequest> requests, PartnerGym partnerGym) {
		List<GymProduct> gymProducts = requests.stream()
			.map(request -> GymProductMapper.toEntity(request, partnerGym))
			.toList();

		gymProductRepository.saveAll(gymProducts);
	}

	// 헬스장 이용권 정보 수정
	// @Transactional
	// public void updateGymProducts(List<GymProductRequest> requests, PartnerGym partnerGym) {
	// 	// 기존 이용권들 조회
	// 	List<GymProduct> existingProducts = gymProductRepository.findByPartnerGym(partnerGym);
	//
	// 	// ID 기준으로 매핑
	// 	Map<Long, GymProduct> existingMap = existingProducts.stream()
	// 		.collect(Collectors.toMap(GymProduct::getGymProductId, Function.identity()));
	//
	// 	List<GymProduct> productsToSave = new ArrayList<>();
	//
	// 	for (GymProductRequest request : requests) {
	// 		if (request.gymProductId() == null) {
	// 			// 새로 추가되는 이용권
	// 			GymProduct newProduct = GymProductMapper.toEntity(request, partnerGym);
	// 			productsToSave.add(newProduct);
	// 		} else {
	// 			// 기존 이용권 수정
	// 			GymProduct existing = existingMap.remove(request.gymProductId());
	// 			existing.update(request); // 엔티티 내부 update 메서드
	// 			productsToSave.add(existing);
	// 		}
	// 	}
	//
	// 	// 기존 목록 중 남은 것들은 요청에 없던 것 → 삭제 대상
	// 	// TODO: 헬스장 이용권 정보 삭제 메서드 호출
	//
	// 	// 저장
	// 	gymProductRepository.saveAll(productsToSave);
	// }

	// 헬스장 이용권 정보 삭제
	//TODO: 메서드 작성(GYMMATE-190)

}
