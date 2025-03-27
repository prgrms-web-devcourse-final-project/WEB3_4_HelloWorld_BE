package org.helloworld.gymmate.domain.user.trainer.service;

import org.helloworld.gymmate.domain.user.enumerate.SocialProviderType;
import org.helloworld.gymmate.domain.user.mapper.SocialProviderMapper;
import org.helloworld.gymmate.domain.user.model.SocialProvider;
import org.helloworld.gymmate.domain.user.repository.SocialProviderRepository;
import org.helloworld.gymmate.domain.user.trainer.dto.OwnerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerModifyRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.mapper.TrainerMapper;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerService {
	private final TrainerRepository trainerRepository;
	private final SocialProviderRepository socialProviderRepository;

	@Transactional
	public Long createTrainer(SocialProviderType socialProviderType, String socialProviderId) {
		SocialProvider socialProvider = SocialProviderMapper.toEntity(socialProviderType, socialProviderId);
		socialProviderRepository.save(socialProvider);
		socialProviderRepository.flush();

		Trainer trainer = TrainerMapper.toTrainer(socialProvider);
		return trainerRepository.save(trainer).getTrainerId();
	}

	@Transactional
	public Long createOwner(SocialProviderType socialProviderType, String socialProviderId) {
		SocialProvider socialProvider = SocialProviderMapper.toEntity(socialProviderType, socialProviderId);
		socialProviderRepository.save(socialProvider);
		socialProviderRepository.flush();

		Trainer owner = TrainerMapper.toOwner(socialProvider);
		return trainerRepository.save(owner).getTrainerId();
	}

	// 추가 정보 등록 (직원)
	@Transactional
	public void registerInfoByTrainer(Trainer trainer, TrainerRegisterRequest request) {
		trainer.updateTrainerInfo(request);
		trainerRepository.save(trainer);
	}

	// 추가 정보 등록 (사장)
	@Transactional
	public void registerInfoByOwner(Trainer trainer, OwnerRegisterRequest request) {
		trainer.updateOwnerInfo(request);
		trainerRepository.save(trainer);
	}

	// 추가 정보 등록 여부 확인
	@Transactional(readOnly = true)
	public boolean check(Trainer trainer) {
		return trainer.getAdditionalInfoCompleted();
	}

	// 직원 및 사장 개인정보 수정
	@Transactional
	public Long modifyTrainerInfo(Trainer trainer, TrainerModifyRequest modifyRequest) {
		trainer.modifyTrainerInfo(modifyRequest);
		return trainerRepository.save(trainer).getTrainerId();
	}

	// 직원 및 사장 삭제
	// 추후 추가되는 기능에 따라 수정 필요
	@Transactional
	public void deleteTrainer(Trainer trainer) {
		trainerRepository.delete(trainer);
	}
}
