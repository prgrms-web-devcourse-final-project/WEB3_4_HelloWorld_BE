package org.helloworld.gymmate.domain.gym.gymInfo.service;

import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.facility.dto.FacilityResponse;
import org.helloworld.gymmate.domain.gym.facility.mapper.FacilityMapper;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gymInfo.repository.GymRepository;
import org.helloworld.gymmate.domain.gym.machine.dto.FacilityAndMachineResponse;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineResponse;
import org.helloworld.gymmate.domain.gym.machine.mapper.MachineMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymService {
	private final GymRepository gymRepository;

	@Transactional(readOnly = true)
	public FacilityAndMachineResponse getOwnFacilitiesAndMachines(Long gymId) {
		Gym gym = getExistingGym(gymId);
		FacilityResponse facilityResponse = getFacility(gym);
		List<MachineResponse> machineResponses = MachineMapper.toDtoList(gym.getMachines());
		return new FacilityAndMachineResponse(facilityResponse, machineResponses);
	}

	// 헬스장 조회
	public Gym getExistingGym(Long gymId) {
		return gymRepository.findById(gymId)
			.orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));
	}

	// 편의시설 조회
	@Transactional(readOnly = true)
	public FacilityResponse getFacility(Gym gym) {
		return FacilityMapper.toDto(gym.getFacility());
	}
	
}
