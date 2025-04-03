package org.helloworld.gymmate.domain.user.trainer.service;

import java.util.Optional;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gymInfo.repository.GymRepository;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.domain.user.trainer.business.service.BusinessValidateService;
import org.helloworld.gymmate.domain.user.trainer.dto.OwnerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerCheckResponse;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerListResponse;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerModifyRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerProfileRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerRegisterRequest;
import org.helloworld.gymmate.domain.user.trainer.dto.TrainerResponse;
import org.helloworld.gymmate.domain.user.trainer.enums.TrainerSearchOption;
import org.helloworld.gymmate.domain.user.trainer.enums.TrainerSortOption;
import org.helloworld.gymmate.domain.user.trainer.mapper.TrainerMapper;
import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.helloworld.gymmate.security.oauth.repository.OauthRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	private final MemberService memberService;

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

	public Page<TrainerListResponse> getTrainers(String sortOption, String searchOption, String searchTerm,
		int page, int pageSize, Double x, Double y) {
		TrainerSortOption sort = TrainerSortOption.from(sortOption);
		TrainerSearchOption search = TrainerSearchOption.from(searchOption);
		Pageable pageable = PageRequest.of(page, pageSize);

		return switch (sort) {
			case LATEST -> fetchLatestTrainers(search, searchTerm, pageable);
			case SCORE -> fetchScoreSortedTrainers(search, searchTerm, pageable);
			case NEARBY -> fetchNearbyTrainersUsingXY(search, searchTerm, pageable, x, y);
		};
	}

	private Page<TrainerListResponse> fetchLatestTrainers(TrainerSearchOption search, String searchTerm,
		Pageable pageable) {
		Page<Trainer> trainers = switch (search) {
			case NONE -> trainerRepository.findAllByOrderByTrainerIdDesc(pageable);
			case TRAINER ->
				trainerRepository.findByTrainerNameContainingIgnoreCaseOrderByTrainerIdDesc(searchTerm, pageable);
			case DISTRICT -> trainerRepository.findByGymAddressOrderByTrainerIdDesc(searchTerm, pageable);
		};
		return fetchAndMapTrainers(trainers, pageable);
	}

	// TODO :
	private Page<TrainerListResponse> fetchScoreSortedTrainers(TrainerSearchOption search, String searchTerm,
		Pageable pageable) {
		return null;
	}

	public Page<TrainerListResponse> getNearbyTrainers(String searchOption, String searchTerm, int page,
		int pageSize, CustomOAuth2User customOAuth2User) {
		TrainerSearchOption search = TrainerSearchOption.from(searchOption);
		Member member = memberService.findByUserId(customOAuth2User.getUserId());
		Double x = Double.valueOf(member.getXField());
		Double y = Double.valueOf(member.getYField());
		Pageable pageable = PageRequest.of(page, pageSize);

		return fetchNearbyTrainersUsingXY(search, searchTerm, pageable, x, y);
	}

	private Page<TrainerListResponse> fetchNearbyTrainersUsingXY(TrainerSearchOption trainerSearchOption,
		String searchTerm, Pageable pageable, Double x, Double y) {
		String searchValue = (trainerSearchOption == TrainerSearchOption.NONE) ? "" : searchTerm;
		Page<Trainer> trainers = trainerRepository.findNearbyTrainersWithSearch(x, y,
			trainerSearchOption.name(),
			searchValue, pageable);
		return fetchAndMapTrainers(trainers, pageable);
	}

	// TODO :
	@Transactional(readOnly = true)
	protected Page<TrainerListResponse> fetchAndMapTrainers(Page<Trainer> trainers, Pageable pageable) {
		// 트레이너 정보 + 수상이력 = DTO 변환해서 반환
		return null;
	}
}
