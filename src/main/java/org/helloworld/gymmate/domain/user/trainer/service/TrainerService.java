package org.helloworld.gymmate.domain.user.trainer.service;

import java.util.Optional;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gymInfo.repository.GymRepository;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.trainer.business.service.BusinessValidateService;
import org.helloworld.gymmate.domain.user.trainer.dto.OwnerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerCheckResponse;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerModifyRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerProfileRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerResponse;
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
	public Long registerInfoByTrainer(Trainer trainer, TrainerRegisterRequest request) {
		trainer.registerTrainerInfo(request);
		Gym gym = gymRepository.findGymByGymName(request.gymName())
			.orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));
		trainer.assignGym(gym);
		return trainerRepository.save(trainer).getTrainerId();
	}

	// 추가 정보 등록 (사장)
	@Transactional
	public Long registerInfoByOwner(Trainer trainer, OwnerRegisterRequest request) {
		businessValidateService.validateBusiness(request);
		trainer.registerOwnerInfo(request);
		return trainerRepository.save(trainer).getTrainerId();
	}

	// 직원 및 사장 개인정보 수정
	@Transactional
	public Long modifyTrainerInfo(Trainer trainer, TrainerModifyRequest modifyRequest) {
		trainer.modifyTrainerInfo(modifyRequest);
		return trainerRepository.save(trainer).getTrainerId();
	}

	// 직원 및 사장 한줄소개, 경력, 전문 분야 입력
	@Transactional
	public Long updateTrainerProfile(Trainer trainer, TrainerProfileRequest profileRequest) {
		trainer.updateTrainerProfile(profileRequest);
		return trainerRepository.save(trainer).getTrainerId();
	}

	// 직원 및 사장 삭제
	// 추후 추가되는 기능에 따라 수정 필요
	@Transactional
	public void deleteTrainer(Trainer trainer) {
		trainerRepository.delete(trainer);
	}

	// 마이페이지 정보
	@Transactional(readOnly = true)
	public TrainerResponse getInfo(Trainer trainer) {
		return TrainerMapper.toResponse(trainer);
	}

	// 사장여부
	@Transactional(readOnly = true)
	public TrainerCheckResponse check(Trainer trainer) {
		return TrainerMapper.toCheckResponse(trainer);
	}

	@Transactional(readOnly = true)
	public Optional<Long> getTrainerIdByOauth(String providerId) {
		return oauthRepository.findByProviderIdAndUserType(providerId, UserType.TRAINER)
			.flatMap(oauth -> trainerRepository.findByOauth(oauth)
				.map(Trainer::getTrainerId));
	}

	public Trainer findByUserId(Long userId) {
		return trainerRepository.findByTrainerId(userId).orElseThrow(() -> new BusinessException(
			ErrorCode.USER_NOT_FOUND));
	}
}
