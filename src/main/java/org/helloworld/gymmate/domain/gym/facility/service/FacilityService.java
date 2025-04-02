package org.helloworld.gymmate.domain.gym.facility.service;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.facility.dto.FacilityRequest;
import org.helloworld.gymmate.domain.gym.facility.entity.Facility;
import org.helloworld.gymmate.domain.gym.facility.repository.FacilityRepository;
import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.gym.repository.GymRepository;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
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
	public Long updateFacility(Trainer trainer, Long gymId, FacilityRequest facilityRequest) {
		if (!trainer.getIsOwner()) { // 사장님이 아니면 에러
			throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
		}
		if (!trainer.getGym().getGymId().equals(gymId)) { // 해당 헬스장 사장님이 아니라면 에러
			throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
		}
		Facility facility = findByGymId(gymId).getFacility();
		facility.update(facilityRequest);
		return facilityRepository.save(facility).getFacilityId();
	}

	// Facility 업데이트 (크롤러 전용)
	@Transactional
	public Long updateFacilityByCrawl(Long gymId, FacilityRequest facilityRequest) {
		Facility facility = findByGymId(gymId).getFacility();
		facility.update(facilityRequest);
		return facilityRepository.save(facility).getFacilityId();
	}

	public Gym findByGymId(Long gymId) {
		return gymRepository.findById(gymId).orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));
	}

}
