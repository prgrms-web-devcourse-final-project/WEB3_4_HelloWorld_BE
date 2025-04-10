package org.helloworld.gymmate.domain.gym.partnergym.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.common.util.StringUtil;
import org.helloworld.gymmate.domain.gym.facility.entity.Facility;
import org.helloworld.gymmate.domain.gym.gyminfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gyminfo.entity.GymImage;
import org.helloworld.gymmate.domain.gym.gyminfo.mapper.GymMapper;
import org.helloworld.gymmate.domain.gym.gyminfo.repository.GymRepository;
import org.helloworld.gymmate.domain.gym.gyminfo.service.GymService;
import org.helloworld.gymmate.domain.gym.gymproduct.service.GymProductService;
import org.helloworld.gymmate.domain.gym.partnergym.dao.PartnerGymNameProjection;
import org.helloworld.gymmate.domain.gym.partnergym.dto.request.GymInfoRequest;
import org.helloworld.gymmate.domain.gym.partnergym.dto.request.GymRegisterRequest;
import org.helloworld.gymmate.domain.gym.partnergym.dto.request.GymUpdateRequest;
import org.helloworld.gymmate.domain.gym.partnergym.dto.response.PartnerGymDetailResponse;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;
import org.helloworld.gymmate.domain.gym.partnergym.mapper.PartnerGymMapper;
import org.helloworld.gymmate.domain.gym.partnergym.repository.PartnerGymRepository;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartnerGymService {

    private final PartnerGymRepository partnerGymRepository;
    private final GymRepository gymRepository;
    private final FileManager fileManager;
    private final GymService gymService;
    private final GymProductService gymProductService;
    private final TrainerService trainerService;

    //파트너헬스장 조회
    public Gym getGymByPartnerGymId(Long partnerGymId) {
        PartnerGym partnerGym = partnerGymRepository.findById(partnerGymId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_GYM_NOT_FOUND));

        return partnerGym.getGym();
    }

    @Transactional
    public Long registerPartnerGym(GymRegisterRequest request, List<MultipartFile> images, Long ownerId) {
        // 운영자 맞는지 확인
        validatePartnerGymOwner(ownerId);

        // 중복 등록 방지
        if (partnerGymRepository.existsByOwnerIdAndGym_GymId(ownerId, request.gymId())) {
            throw new BusinessException(ErrorCode.GYM_ALREADY_EXISTS);
        }

        // gym 있는지 확인
        Gym existingGym = gymService.getExistingGym(request.gymId());

        // partnerGym 저장
        PartnerGym partnerGym = createPartnerGym(ownerId, existingGym);

        // 전체 정보 업데이트
        updatePartnerGymInfos(request.gymInfoRequest(), null, images, existingGym, partnerGym);

        return partnerGym.getPartnerGymId();
    }

    @Transactional
    public Long updatePartnerGym(GymUpdateRequest request, List<MultipartFile> images,
        Long ownerId) {
        // 본인이 운영하는 제휴 헬스장 가져오기
        PartnerGym partnerGym = getPartnerGymByOwnerId(ownerId);

        // partnerGym 로 Gym 가져오기
        Gym existingGym = getGymByPartnerGymId(partnerGym.getPartnerGymId());

        // 전체 정보 업데이트
        updatePartnerGymInfos(request.gymInfoRequest(), request.deleteImageIds(), images, existingGym, partnerGym);

        return partnerGym.getPartnerGymId();
    }

    private void updatePartnerGymInfos(GymInfoRequest gymInfoRequest, List<Long> deleteImageIds,
        List<MultipartFile> images,
        Gym existingGym,
        PartnerGym partnerGym) {
        // gym 업데이트
        GymMapper.updateEntity(existingGym, gymInfoRequest.gymRequest());

        // facility 업데이트
        updateFacility(existingGym, gymInfoRequest);

        // gymImage 업데이트
        updateImages(deleteImageIds, images, existingGym);

        // gymProduct 업데이트
        gymProductService.updateGymProducts(gymInfoRequest, partnerGym);
    }

    // 제휴 헬스장 조회
    @Transactional(readOnly = true)
    public PartnerGymDetailResponse getPartnerGymDetail(Long ownerId) {
        PartnerGym partnerGym = getPartnerGymByOwnerId(ownerId);

        // Lazy 로딩 유도 (실제 접근 시 쿼리 발생)
        partnerGym.getGym().getImages().size();
        partnerGym.getGymProducts().size();

        return PartnerGymMapper.toDto(partnerGym);

    }

    // 가까운 헬스장 조회
    @Transactional(readOnly = true)
    public List<Gym> findNearbyGyms(double longitude, double latitude, double radiusInMeters, int limit) {
        String point = StringUtil.format("POINT({} {})", latitude, longitude);
        return gymRepository.findNearbyGyms(point, radiusInMeters, limit);
    }

    public PartnerGym getPartnerGymByOwnerId(Long ownerId) {
        // 본인이 운영하는 제휴 헬스장 가져오기
        return partnerGymRepository.findByOwnerId(ownerId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_AUTHORIZED));
    }

    private void validatePartnerGymOwner(Long ownerId) {
        Trainer owner = trainerService.findByUserId(ownerId);
        if (!owner.getIsOwner()) {
            throw new BusinessException(ErrorCode.GYM_REGISTRATION_FORBIDDEN);
        }
    }

    private PartnerGym createPartnerGym(Long ownerId, Gym gym) {
        PartnerGym partnerGym = PartnerGym.builder()
            .ownerId(ownerId)
            .gym(gym)
            .build();

        gym.updatePartner(true);

        return partnerGymRepository.save(partnerGym);
    }

    private List<GymImage> uploadAndMapImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty())
            return List.of();

        List<String> imageUrls;
        try {
            imageUrls = fileManager.uploadFiles(images, "gym");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.S3_UPLOAD_FAILED);
        }

        return imageUrls.stream()
            .map(url -> GymImage.builder().url(url).build())
            .toList();
    }

    // 이미지 삭제 + 새 이미지 등록
    private void updateImages(List<Long> deleteImageIds, List<MultipartFile> images, Gym gym) {
        // 이미지 삭제 호출
        if (deleteImageIds != null) {
            deleteImages(deleteImageIds, gym);
        }

        // 새 이미지 등록 호출
        saveImages(images, gym);
    }

    // 신규 이미지 저장
    private void saveImages(List<MultipartFile> images, Gym gym) {
        List<GymImage> gymImages = uploadAndMapImages(images);
        gym.addImages(gymImages);
    }

    private void deleteImages(List<Long> deleteImageIds, Gym gym) {
        // 삭제할 이미지 필터링
        List<GymImage> imagesToDelete = gym.getImages().stream()
            .filter(img -> deleteImageIds.contains(img.getGymImageId()))
            .toList();

        // S3 + 연관관계 제거
        for (GymImage image : imagesToDelete) {
            fileManager.deleteFile(image.getUrl());
            gym.removeImage(image);
        }
    }

    private void updateFacility(Gym existingGym, GymInfoRequest request) {
        Facility facility = existingGym.getFacility();
        facility.update(request.facilityRequest());
    }

    @Transactional(readOnly = true)
    public List<PartnerGymNameProjection> getGymNamesByIds(Collection<Long> partnerGymIds) {
        return partnerGymRepository.findGymNamesByPartnerGymIds(partnerGymIds);
    }

    public PartnerGym getPartnerGymByGymId(Long gymId) {
        return Optional.ofNullable(partnerGymRepository.findByGym_GymId(gymId))
            .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_GYM_NOT_FOUND));
    }
}



