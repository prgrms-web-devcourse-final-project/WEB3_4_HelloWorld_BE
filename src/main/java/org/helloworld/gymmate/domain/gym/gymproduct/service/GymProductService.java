package org.helloworld.gymmate.domain.gym.gymproduct.service;

import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.gymproduct.dto.GymProductRequest;
import org.helloworld.gymmate.domain.gym.gymproduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymproduct.mapper.GymProductMapper;
import org.helloworld.gymmate.domain.gym.gymproduct.repository.GymProductRepository;
import org.helloworld.gymmate.domain.gym.partnergym.dto.request.GymInfoRequest;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GymProductService {

    private final GymProductRepository gymProductRepository;

    /** 헬스장 이용권 정보 업데이트*/
    public void updateGymProducts(GymInfoRequest gymInfoRequest, PartnerGym partnerGym) {
        // 생성 or 수정 호출
        List<GymProductRequest> gymProductRequests = gymInfoRequest.gymProductRequest();
        for (GymProductRequest request : gymProductRequests) {
            if (request.gymProductId() == null) {
                createGymProducts(request, partnerGym);
            } else {
                modifyGymProducts(request, partnerGym);
            }
        }
        // 삭제 호출
        deleteGymProducts(gymInfoRequest.gymProductDeleteIds(), partnerGym);
    }

    /** 헬스장 이용권 정보 생성*/
    public void createGymProducts(GymProductRequest request, PartnerGym partnerGym) {
        gymProductRepository.save(GymProductMapper.toEntity(request, partnerGym));
    }

    /** 헬스장 이용권 정보 수정*/
    public void modifyGymProducts(GymProductRequest request, PartnerGym partnerGym) {
        assert request.gymProductId() != null; // 명시적으로 null 이 아닌 것을 표시

        GymProduct existingProduct = checkGymProductOwnerShip(request.gymProductId(), partnerGym); //소유 검증
        existingProduct.update(request);
    }

    /** 헬스장 이용권 정보 삭제*/
    public void deleteGymProducts(List<Long> deleteIds, PartnerGym partnerGym) {
        for (Long deleteId : deleteIds) {
            checkGymProductOwnerShip(deleteId, partnerGym); //소유 검증
            gymProductRepository.deleteById(deleteId);
        }
    }

    /** 주어진 ID에 해당하는 헬스장 상품(GymProduct)을 조회 */
    public GymProduct findByGymProductId(Long gymProductId) {
        return gymProductRepository.findById(gymProductId).orElseThrow(
            () -> new BusinessException(ErrorCode.GYMPRODUCT_NOT_FOUND)
        );
    }

    /** 해당 이용권이 파트너헬스장에 속하는지 검증*/
    public GymProduct checkGymProductOwnerShip(Long gymProductId, PartnerGym partnerGym) {
        GymProduct gymProduct = gymProductRepository.findById(gymProductId)
            .orElseThrow(() -> new BusinessException(ErrorCode.GYMPRODUCT_NOT_FOUND));

        if (!gymProduct.getPartnerGym().equals(partnerGym)) {
            throw new BusinessException(ErrorCode.GYMPRODUCT_PARTNER_MISMATCH);
        }

        return gymProduct;
    }

}
