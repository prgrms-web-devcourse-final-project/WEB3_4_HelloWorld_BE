package org.helloworld.gymmate.domain.gym.machine.service;

import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.gym.service.GymService;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineCreateRequest;
import org.helloworld.gymmate.domain.gym.machine.entity.Machine;
import org.helloworld.gymmate.domain.gym.machine.mapper.MachineMapper;
import org.helloworld.gymmate.domain.gym.machine.repository.MachineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MachineService {
	private final FileManager fileManager;
	private final MachineRepository machineRepository;
	private final GymService gymService;

	@Transactional
	public Long createMachine(Long trainerId, MachineCreateRequest request, MultipartFile image) {
		Gym gym = gymService.findGymByTrainerId(trainerId);
		String imageUrl = (image != null && !image.isEmpty())
			? fileManager.uploadFile(image, "machine")
			: null; // TODO : 이미지 무조건 받을건지
		return insertMachine(request, imageUrl, gym).getMachineId();
	}

	@Transactional // 크롤러 용도로도 사용
	public Machine insertMachine(MachineCreateRequest request, String imageUrl, Gym gym) {
		Machine machine = MachineMapper.toEntity(request, imageUrl, gym);
		machineRepository.save(machine);
		return machine;
	}

}
