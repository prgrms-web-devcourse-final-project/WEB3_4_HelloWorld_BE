package org.helloworld.gymmate.domain.user.trainer.service;

import java.util.Optional;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.gym.repository.GymRepository;
import org.helloworld.gymmate.domain.user.trainer.business.service.BusinessValidateService;
import org.helloworld.gymmate.domain.user.trainer.dto.OwnerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerModifyRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.mapper.TrainerMapper;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.helloworld.gymmate.security.oauth.repository.OauthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerService {
	private final TrainerRepository trainerRepository;
	private final OauthRepository oauthRepository;
	private final EntityManager entityManager;
	private final GymRepository gymRepository;
	private final BusinessValidateService businessValidateService;

	@Transactional
	public Long createTrainer(Oauth oauth) {
		if (!entityManager.contains(oauth)) {
			oauth = entityManager.merge(oauth);
		}
		Trainer trainer = TrainerMapper.toTrainer(oauth);
		return trainerRepository.save(trainer).getTrainerId();
	}

	// 추가 정보 등록 (직원)
	@Transactional
	public void registerInfoByTrainer(Trainer trainer, TrainerRegisterRequest request) {
		trainer.registerTrainerInfo(request);
		Gym gym = gymRepository.findGymByGymName(request.gymName())
			.orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));
		trainer.assignGym(gym);
		trainerRepository.save(trainer);
	}

	// 추가 정보 등록 (사장)
	@Transactional
	public void registerInfoByOwner(Trainer trainer, OwnerRegisterRequest request) {
		businessValidateService.validateBusiness(request);
		trainer.registerOwnerInfo(request);
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

	@Transactional(readOnly = true)
	public Optional<Long> getTrainerIdByOauth(String providerId) {
		return oauthRepository.findByProviderId(providerId)
			.flatMap(oauth -> trainerRepository.findByOauth(oauth)
				.map(Trainer::getTrainerId));
	}

	public Trainer findByUserId(Long userId) {
		return trainerRepository.findByTrainerId(userId).orElseThrow(() -> new BusinessException(
			ErrorCode.USER_NOT_FOUND));
	}
}
