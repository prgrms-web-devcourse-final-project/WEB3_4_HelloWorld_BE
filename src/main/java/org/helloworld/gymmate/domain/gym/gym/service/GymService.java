package org.helloworld.gymmate.domain.gym.gym.service;

import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.gym.gym.dto.GymCreateRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.GymResponse;
import org.helloworld.gymmate.domain.gym.gym.dto.GymUpdateRequest;
import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.gym.entity.GymImage;
import org.helloworld.gymmate.domain.gym.gym.mapper.GymMapper;
import org.helloworld.gymmate.domain.gym.gym.repository.GymRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymService {

	private final GymRepository gymRepository;
	private final FileManager fileManager;

	@Transactional
	public GymResponse createGym(GymCreateRequest request, List<MultipartFile> images) {



		List<String> imageUrls = fileManager.uploadFiles(images, "gym");

		GymCreateRequest updatedRequest = new GymCreateRequest(
			request.gymName(),
			request.startTime(),
			request.endTime(),
			request.phoneNumber(),
			request.address(),
			request.xField(),
			request.yField(),
			request.intro(),
			imageUrls
		);

		Gym gym = GymMapper.toEntity(updatedRequest);
		Gym saved = gymRepository.save(gym);
		return GymMapper.toResponse(saved);
	}

	@Transactional
	public GymResponse updateGym(Long gymId, GymUpdateRequest request, List<MultipartFile> images) {

		Gym gym = gymRepository.findById(gymId)
			.orElseThrow(()-> new BusinessException(ErrorCode.GYM_NOT_FOUND));

		GymMapper.updateEntity(gym, request);

		//기존 이미지 중 삭제 대상 S3 삭제
		List<String> keepUrls = request.existingImageUrls() != null ? request.existingImageUrls() : List.of();

		List<GymImage> imagesToDelete = gym.getImages().stream()
			.filter(img -> !keepUrls.contains(img.getUrl()))
			.toList();

		for (GymImage image : imagesToDelete) {
			fileManager.deleteFile(image.getUrl());
		}

		// DB에서 삭제 대상 제거하고 유지할 이미지만 남기기
		List<GymImage> imagesToKeep = gym.getImages().stream()
			.filter(img -> keepUrls.contains(img.getUrl()))
			.toList();

		gym.clearImages();
		gym.addImages(imagesToKeep);

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


}


