package org.helloworld.gymmate.domain.gym.gym.service;

import java.util.List;

import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.gym.gym.dto.GymCreateRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.GymResponse;
import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
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

		List<String> imageUrls = fileManager.uploadFiles(images, "gymImage");

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
}
