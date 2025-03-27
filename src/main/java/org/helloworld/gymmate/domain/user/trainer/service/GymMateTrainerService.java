package org.helloworld.gymmate.domain.user.trainer.service;

import org.helloworld.gymmate.domain.user.enumerate.SocialProviderType;
import org.helloworld.gymmate.domain.user.mapper.SocialProviderMapper;
import org.helloworld.gymmate.domain.user.model.SocialProvider;
import org.helloworld.gymmate.domain.user.repository.SocialProviderRepository;
import org.helloworld.gymmate.domain.user.trainer.dto.OwnerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerModifyRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.mapper.GymMateTrainerMapper;
import org.helloworld.gymmate.domain.user.trainer.model.GymmateTrainer;
import org.helloworld.gymmate.domain.user.trainer.repository.GymMateTrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymMateTrainerService {
	private final GymMateTrainerRepository trainerRepository;
	private final SocialProviderRepository socialProviderRepository;

	@Transactional
	public Long createTrainer(SocialProviderType socialProviderType, String socialProviderId) {
		SocialProvider socialProvider = SocialProviderMapper.toEntity(socialProviderType, socialProviderId);
		socialProviderRepository.save(socialProvider);
		socialProviderRepository.flush();

		GymmateTrainer trainer = GymMateTrainerMapper.toTrainer(socialProvider);
		return trainerRepository.save(trainer).getTrainerId();
	}

	@Transactional
	public Long createOwner(SocialProviderType socialProviderType, String socialProviderId) {
		SocialProvider socialProvider = SocialProviderMapper.toEntity(socialProviderType, socialProviderId);
		socialProviderRepository.save(socialProvider);
		socialProviderRepository.flush();

		GymmateTrainer owner = GymMateTrainerMapper.toOwner(socialProvider);
		return trainerRepository.save(owner).getTrainerId();
	}

	// 추가 정보 등록 (직원)
	@Transactional
	public void registerInfoByTrainer(GymmateTrainer gymmateTrainer, TrainerRegisterRequest request) {
		gymmateTrainer.updateTrainerInfo(request);
		trainerRepository.save(gymmateTrainer);
	}

	// 추가 정보 등록 (사장)
	@Transactional
	public void registerInfoByOwner(GymmateTrainer gymmateTrainer, OwnerRegisterRequest request) {
		gymmateTrainer.updateOwnerInfo(request);
		trainerRepository.save(gymmateTrainer);
	}

	// 추가 정보 등록 여부 확인
	@Transactional(readOnly = true)
	public boolean check(GymmateTrainer gymmateTrainer) {
		return gymmateTrainer.getAdditionalInfoCompleted();
	}

	// 직원 및 사장 개인정보 수정
	public Long modifyTrainerInfo(GymmateTrainer gymmateTrainer, TrainerModifyRequest modifyRequest) {
		gymmateTrainer.modifyTrainerInfo(modifyRequest);
		return trainerRepository.save(gymmateTrainer).getTrainerId();
	}

}
