package org.helloworld.gymmate.domain.gym.machine.service;

import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gymInfo.service.GymService;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineCreateRequest;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineResponse;
import org.helloworld.gymmate.domain.gym.machine.entity.Machine;
import org.helloworld.gymmate.domain.gym.machine.mapper.MachineMapper;
import org.helloworld.gymmate.domain.gym.machine.repository.MachineRepository;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MachineService {
	private final TrainerService trainerService;
	private final FileManager fileManager;
	private final MachineRepository machineRepository;
	private final GymService gymService;

	@Transactional
	public Long createMachine(Long trainerId, MachineCreateRequest request, MultipartFile image) {
		Trainer trainer = ownerCheck(trainerId);
		String imageUrl = (image != null && !image.isEmpty())
			? fileManager.uploadFile(image, "machine")
			: null; // TODO : 이미지 무조건 받을건지
		return insertMachine(request, imageUrl, trainer.getGym()).getMachineId();
	}

	@Transactional // 크롤러 용도로도 사용
	public Machine insertMachine(MachineCreateRequest request, String imageUrl, Gym gym) {
		Machine machine = MachineMapper.toEntity(request, imageUrl, gym);
		return machineRepository.save(machine);
	}

	private Trainer ownerCheck(Long trainerId) {
		Trainer trainer = trainerService.findByUserId(trainerId);
		if (!trainer.getIsOwner()) {
			throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
		}
		return trainer;
	}

	@Transactional
	public void deleteMachines(Long trainerId, Long machineId) {
		Trainer trainer = ownerCheck(trainerId);
		Machine machine = findByMachineId(machineId);
		if (!trainer.getGym().getMachines().remove(machine)) { // 해당 gym에 머신이 속하지 않았을 경우
			throw new BusinessException(ErrorCode.MACHINE_FORBIDDEN);
		}
	}

	public Machine findByMachineId(Long machineId) {
		return machineRepository.findById(machineId)
			.orElseThrow(() -> new BusinessException(ErrorCode.MACHINE_NOT_FOUND));
	}

	public List<MachineResponse> getOwnMachines(Long trainerId) {
		Trainer trainer = ownerCheck(trainerId);
		return MachineMapper.toDtoList(trainer.getGym().getMachines());
	}
}
