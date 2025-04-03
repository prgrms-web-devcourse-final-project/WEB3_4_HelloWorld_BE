package org.helloworld.gymmate.domain.gym.gymInfo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.common.util.StringUtil;
import org.helloworld.gymmate.domain.gym.gymInfo.dto.request.RegisterGymRequest;
import org.helloworld.gymmate.domain.gym.gymInfo.dto.request.UpdateGymRequest;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.GymImage;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.PartnerGym;
import org.helloworld.gymmate.domain.gym.gymInfo.mapper.GymMapper;
import org.helloworld.gymmate.domain.gym.gymInfo.repository.GymRepository;
import org.helloworld.gymmate.domain.gym.gymInfo.repository.PartnerGymRepository;
import org.helloworld.gymmate.domain.gym.gymProduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymProduct.mapper.GymProductMapper;
import org.helloworld.gymmate.domain.gym.gymProduct.repository.GymProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymService {

	private final PartnerGymRepository partnerGymRepository;
	private final GymRepository gymRepository;
	private final FileManager fileManager;
	private final GymProductRepository gymProductRepository;

	//헬스장 조회(공통 코드)
	public Gym getExistingGym(Long gymId) {
		return gymRepository.findById(gymId)
			.orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));
	}

	@Transactional
	public Long registerPartnerGym(RegisterGymRequest request, List<MultipartFile> images, Long ownerId) {
		// 운영자 맞는지 확인
		// TODO: 메소드 호출 (trainer테이블에서 isOwner가 true인지 파악) seyeon

		// 중복 등록 방지
		if (partnerGymRepository.existsByOwnerIdAndGym_GymId(ownerId, request.gymId())) {
			throw new BusinessException(ErrorCode.GYM_ALREADY_EXISTS);
		}

		// gym 있는지 확인
		Gym existingGym = getExistingGym(request.gymId());

		// gym 업데이트
		GymMapper.updateEntity(existingGym, request.gymInfoRequest().gymRequest());

		// facility 업데이트
		//TODO: 메소드 호출

		// gymImage 업데이트
		//TODO: 메소드 호출 seyeon
		saveImages(images, existingGym);

		//machineImage 업데이트
		//TODO: 메소드 호출

		//gymProduct 업데이트
		List<GymProduct> gymProducts = request.gymProductRequest().stream()
			.map(GymProductMapper::toEntityWithoutPartnerGym) // partnerGym 없이 생성
			.collect(Collectors.toList());

		gymProductRepository.saveAll(gymProducts);  // DB에는 partner_gym_id = null로 저장

		// partnerGym 저장
		PartnerGym partnerGym = createPartnerGym(ownerId, existingGym);

		for (GymProduct gymProduct : gymProducts) {
			partnerGym.addGymProduct(gymProduct);
		}

		return partnerGym.getPartnerGymId();
	}

	@Transactional
	public Long updatePartnerGym(Long gymId, UpdateGymRequest request, List<MultipartFile> images, Long ownerId) {
		// 운영자 맞는지 확인
		// TODO: 메소드 호출 (trainer테이블에서 isOwner가 true인지 파악) seyeon

		// 기존 gym 가져오기
		Gym existingGym = getExistingGym(gymId);

		// gym 업데이트
		GymMapper.updateEntity(existingGym, request.gymInfoRequest().gymRequest());

		// facility 업데이트
		//TODO: 메소드 호출

		// gymImage 업데이트
		updateImages(request, images, existingGym);

		//machineImage 업데이트
		//TODO: 메소드 호출

		return gymRepository.save(existingGym).getGymId();
	}

	// 가까운 헬스장 조회
	@Transactional(readOnly = true)
	public List<Gym> findNearbyGyms(double longitude, double latitude, double radiusInMeters, int limit) {
		String point = StringUtil.format("POINT({} {})", latitude, longitude);
		return gymRepository.findNearbyGyms(point, radiusInMeters, limit);
	}

	private PartnerGym createPartnerGym(Long ownerId, Gym gym) {
		PartnerGym partnerGym = PartnerGym.builder()
			.ownerId(ownerId)
			.gym(gym)
			.build();

		return partnerGymRepository.save(partnerGym);
	}

	//신규 이미지 저장만 처리
	private void saveImages(List<MultipartFile> images, Gym gym) {
		if (images == null || images.isEmpty())
			return;

		// 유효성 검사
		for (MultipartFile image : images) {
			if (image.getSize() > 5 * 1024 * 1024) {
				throw new BusinessException(ErrorCode.IMAGE_TOO_LARGE);
			}
			String contentType = image.getContentType();
			if (!List.of("image/jpeg", "image/png", "image/gif").contains(contentType)) {
				throw new BusinessException(ErrorCode.IMAGE_UNSUPPORTED_TYPE);
			}
		}

		// 업로드
		List<String> imageUrls;
		try {
			imageUrls = fileManager.uploadFiles(images, "gym");
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.S3_UPLOAD_FAILED);
		}

		// DB 반영
		List<GymImage> gymImages = imageUrls.stream()
			.map(url -> GymImage.builder().url(url).build())
			.toList();

		gym.addImages(gymImages);
	}

	//	이미지 삭제 + 새 이미지 등록 둘 다 처리
	private void updateImages(UpdateGymRequest request, List<MultipartFile> images, Gym gym) {
		// 삭제할 이미지 ID 목록
		List<Long> deleteImageIds = request.deleteImageIds() != null ? request.deleteImageIds() : List.of();

		List<GymImage> imagesToDelete = gym.getImages().stream()
			.filter(img -> deleteImageIds.contains(img.getId()))
			.toList();

		// 삭제 (S3 + DB 관계 제거)
		for (GymImage image : imagesToDelete) {
			fileManager.deleteFile(image.getUrl());
			gym.removeImage(image);
		}

		// 4. 이미지 처리
		if (images != null && !images.isEmpty()) {

			// 4-1. 이미지 유효성 검사
			for (MultipartFile image : images) {
				if (image.getSize() > 5 * 1024 * 1024) {
					throw new BusinessException(ErrorCode.IMAGE_TOO_LARGE);
				}
				String contentType = image.getContentType();
				if (!List.of("image/jpeg", "image/png", "image/gif").contains(contentType)) {
					throw new BusinessException(ErrorCode.IMAGE_UNSUPPORTED_TYPE);
				}
			}

			// 4-2. 이미지 업로드
			List<String> imageUrls;
			try {
				imageUrls = fileManager.uploadFiles(images, "gym");
			} catch (Exception e) {
				throw new BusinessException(ErrorCode.S3_UPLOAD_FAILED);
			}

			List<GymImage> newImages = imageUrls.stream()
				.map(url -> GymImage.builder().url(url).build())
				.toList();

			gym.addImages(newImages);
		}
	}
}


