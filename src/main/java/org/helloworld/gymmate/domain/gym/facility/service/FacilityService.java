package org.helloworld.gymmate.domain.gym.facility.service;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.facility.dto.FacilityRequest;
import org.helloworld.gymmate.domain.gym.facility.entity.Facility;
import org.helloworld.gymmate.domain.gym.facility.repository.FacilityRepository;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gymInfo.repository.GymRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacilityService {
	private final FacilityRepository facilityRepository;
	private final GymRepository gymRepository;

	// Facility 업데이트
	@Transactional
	public Facility updateFacility(Long gymId, FacilityRequest facilityRequest) {
		Facility facility = findByGymId(gymId).getFacility();
		facility.update(facilityRequest);
		return facilityRepository.save(facility);
	}

	public Gym findByGymId(Long gymId) {
		return gymRepository.findById(gymId).orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));
	}

}
