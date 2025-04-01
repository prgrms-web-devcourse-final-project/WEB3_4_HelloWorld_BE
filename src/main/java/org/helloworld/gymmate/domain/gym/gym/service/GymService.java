package org.helloworld.gymmate.domain.gym.gym.service;

import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.common.util.StringUtil;
import org.helloworld.gymmate.domain.gym.gym.dto.request.GymUpdateRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.request.PartnerGymRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.response.GymResponse;
import org.helloworld.gymmate.domain.gym.gym.dto.response.PartnerGymResponse;
import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.gym.entity.GymImage;
import org.helloworld.gymmate.domain.gym.gym.entity.PartnerGym;
import org.helloworld.gymmate.domain.gym.gym.mapper.GymMapper;
import org.helloworld.gymmate.domain.gym.gym.repository.GymRepository;
import org.helloworld.gymmate.domain.gym.gym.repository.PartnerGymRepository;
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

	//헬스장 조회(공통 코드)
	public Gym getGymOrThrow(Long gymId) {
		return gymRepository.findById(gymId)
			.orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));
	}

	@Transactional
	public PartnerGymResponse registerPartnerGym(PartnerGymRequest request, Long ownerId) {

		// 중복 등록 방지
		if (partnerGymRepository.existsByOwnerIdAndGym_GymId(ownerId, request.gymId())) {
			throw new BusinessException(ErrorCode.GYM_ALREADY_EXISTS);
		}

		// gym 조회
		Gym gym = getGymOrThrow(request.gymId());

		// partnerGym 저장
		PartnerGym partnerGym = PartnerGym.builder()
			.ownerId(ownerId)
			.gym(gym)
			.build();

		PartnerGym saved = partnerGymRepository.save(partnerGym);
		return new PartnerGymResponse(saved.getPartnerGymId());
	}

	@Transactional
	public GymResponse updateGym(Long gymId, GymUpdateRequest request, List<MultipartFile> images) {

		Gym gym = getGymOrThrow(gymId);

		GymMapper.updateEntity(gym, request);

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

		return GymMapper.toResponse(gym);
	}

	@Transactional(readOnly = true)
	public List<Gym> findNearbyGyms(double longitude, double latitude, double radiusInMeters, int limit) {
		String point = StringUtil.format("POINT({} {})", latitude, longitude);
		return gymRepository.findNearbyGyms(point, radiusInMeters, limit);
	}

}


