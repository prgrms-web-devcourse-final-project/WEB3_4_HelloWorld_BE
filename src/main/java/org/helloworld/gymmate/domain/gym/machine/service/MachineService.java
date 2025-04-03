package org.helloworld.gymmate.domain.gym.machine.service;

import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.common.s3.FileManager;
import org.helloworld.gymmate.domain.gym.facility.dto.FacilityResponse;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gymInfo.service.GymService;
import org.helloworld.gymmate.domain.gym.machine.dto.FacilityAndMachineResponse;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineRequest;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineResponse;
import org.helloworld.gymmate.domain.gym.machine.entity.Machine;
import org.helloworld.gymmate.domain.gym.machine.mapper.MachineMapper;
import org.helloworld.gymmate.domain.gym.machine.repository.MachineRepository;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MachineService {
	private final TrainerService trainerService;
	private final FileManager fileManager;
	private final MachineRepository machineRepository;
	private final GymService gymService;

	// 환경변수화 할지
	private final int MACHINE_MAX_SIZE = 30;

	@Transactional
	public Long createMachine(Long trainerId, MachineRequest request, MultipartFile image) {
		Trainer trainer = ownerCheck(trainerId);
		if (machineRepository.countByGymId(trainer.getGym().getGymId()) >= MACHINE_MAX_SIZE) {
			throw new BusinessException(ErrorCode.MACHINE_MAX_UPLOAD);
		}
		String imageUrl = (image != null && !image.isEmpty())
			? fileManager.uploadFile(image, "machine")
			: null; // TODO : 이미지 무조건 받을건지
		return insertMachine(request, imageUrl, trainer.getGym()).getMachineId();
	}

	@Transactional // 크롤러 용도로도 사용
	public Machine insertMachine(MachineRequest request, String imageUrl, Gym gym) {
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

	@Transactional(readOnly = true)
	public Machine findByMachineId(Long machineId) {
		return machineRepository.findById(machineId)
			.orElseThrow(() -> new BusinessException(ErrorCode.MACHINE_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public List<MachineResponse> getOwnMachines(Long trainerId) {
		Trainer trainer = ownerCheck(trainerId);
		return MachineMapper.toDtoList(trainer.getGym().getMachines());
	}

	@Transactional(readOnly = true)
	public FacilityAndMachineResponse getOwnFacilitiesAndMachines(Long gymId) {
		FacilityResponse facilityResponse = gymService.getFacility(gymId);
		// TODO : gym 조회가 두번 일어나서 뭔가 수정하고 싶음.
		List<MachineResponse> machineResponses = MachineMapper.toDtoList(
			gymService.getExistingGym(gymId).getMachines());
		return new FacilityAndMachineResponse(facilityResponse, machineResponses);
	}

	@Transactional
	public Long modifyMachine(Long userId, @Valid MachineRequest request, MultipartFile image, Long machineId) {
		Machine machine = findByMachineId(machineId);
		machine.update(request);
		if (image != null) {
			fileManager.deleteFile(machine.getMachineImage());
			String imageUrl = fileManager.uploadFile(image, "machine");
			machine.updateImage(imageUrl);
		}
		return machineId;
	}
}
