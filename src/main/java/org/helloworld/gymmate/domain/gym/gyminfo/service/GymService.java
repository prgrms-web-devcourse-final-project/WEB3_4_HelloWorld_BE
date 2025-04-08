package org.helloworld.gymmate.domain.gym.gyminfo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.enums.GymSearchOption;
import org.helloworld.gymmate.domain.gym.enums.GymSortOption;
import org.helloworld.gymmate.domain.gym.facility.dto.FacilityResponse;
import org.helloworld.gymmate.domain.gym.facility.mapper.FacilityMapper;
import org.helloworld.gymmate.domain.gym.gyminfo.dto.response.GymDetailResponse;
import org.helloworld.gymmate.domain.gym.gyminfo.dto.response.GymListResponse;
import org.helloworld.gymmate.domain.gym.gyminfo.dto.response.TrainerDetailResponse;
import org.helloworld.gymmate.domain.gym.gyminfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gyminfo.mapper.GymMapper;
import org.helloworld.gymmate.domain.gym.gyminfo.repository.GymRepository;
import org.helloworld.gymmate.domain.gym.machine.dto.FacilityAndMachineResponse;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineResponse;
import org.helloworld.gymmate.domain.gym.machine.mapper.MachineMapper;
import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.ptproduct.repository.PtProductRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.award.repository.AwardRepository;
import org.helloworld.gymmate.domain.user.trainer.mapper.TrainerMapper;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.helloworld.gymmate.security.oauth.entity.CustomOAuth2User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymService {
	private final GymRepository gymRepository;
	private final MemberService memberService;
	private final TrainerRepository trainerRepository;
	private final TrainerMapper trainerMapper;
	private final AwardRepository awardRepository;
	private final PtProductRepository ptProductRepository;

	@Transactional(readOnly = true)
	public FacilityAndMachineResponse getOwnFacilitiesAndMachines(Long gymId) {
		Gym gym = getExistingGym(gymId);
		FacilityResponse facilityResponse = getFacility(gym);
		List<MachineResponse> machineResponses = MachineMapper.toDtoList(gym.getMachines());
		return new FacilityAndMachineResponse(facilityResponse, machineResponses);
	}

	// 헬스장 조회 (외부에서 호출하는 부분이 있어서 Transactional 추가)
	@Transactional(readOnly = true)
	public Gym getExistingGym(Long gymId) {
		return gymRepository.findById(gymId)
			.orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));
	}

	// 편의시설 조회
	private FacilityResponse getFacility(Gym gym) {
		return FacilityMapper.toDto(gym.getFacility());
	}

	@Transactional(readOnly = true)
	public Page<GymListResponse> getGyms(String sortOption, String searchOption, String searchTerm,
		int page, int pageSize, Double x, Double y, Boolean isPartner) {
		GymSortOption sort = GymSortOption.from(sortOption);
		GymSearchOption search = GymSearchOption.from(searchOption);
		Pageable pageable = PageRequest.of(page, pageSize);

		return switch (sort) {
			case SCORE -> fetchScoreSortedGyms(search, searchTerm, pageable, isPartner);
			case NEARBY -> fetchNearbyGymsUsingXY(search, searchTerm, pageable, x, y, isPartner);
		};
	}

	@Transactional(readOnly = true)
	public Page<GymListResponse> fetchScoreSortedGyms(GymSearchOption search, String searchTerm,
		Pageable pageable, Boolean isPartner) {
		Page<Gym> gyms = switch (search) {
			case NONE -> gymRepository.findAll(isPartner, pageable);
			case GYM -> gymRepository.searchGymByGymName(searchTerm, isPartner,
				pageable);
			case DISTRICT -> gymRepository.searchGymByAddress(searchTerm, isPartner, pageable);
		};
		return fetchAndMapGyms(gyms, pageable);
	}

	@Transactional(readOnly = true)
	public Page<GymListResponse> getNearbyGyms(String searchOption, String searchTerm, int page,
		int pageSize, Boolean isPartner, CustomOAuth2User customOAuth2User) {
		GymSearchOption search = GymSearchOption.from(searchOption);
		Member member = memberService.findByUserId(customOAuth2User.getUserId());
		Double x = Double.valueOf(member.getXField());
		Double y = Double.valueOf(member.getYField());
		Pageable pageable = PageRequest.of(page, pageSize);
		return fetchNearbyGymsUsingXY(search, searchTerm, pageable, x, y, isPartner);
	}

	@Transactional(readOnly = true)
	public Page<GymListResponse> fetchNearbyGymsUsingXY(GymSearchOption gymSearchOption,
		String searchTerm, Pageable pageable, Double x, Double y, Boolean isPartner) {
		String searchValue = (gymSearchOption == GymSearchOption.NONE) ? "" : searchTerm;
		Page<Gym> gyms = gymRepository.findNearByGymWithSearchAndIsPartner(x, y,
			gymSearchOption.name(),
			searchValue, isPartner, pageable);

		return fetchAndMapGyms(gyms, pageable);
	}

	@Transactional(readOnly = true)
	public Page<GymListResponse> fetchAndMapGyms(Page<Gym> gyms, Pageable pageable) {
		List<GymListResponse> responses = gyms.stream()
			.map(GymMapper::toListResponse)
			.toList();

		return new PageImpl<>(responses, pageable, gyms.getTotalElements());
	}

	@Transactional(readOnly = true)
	public GymDetailResponse getDetail(Long gymId) {
		Gym gym = getExistingGym(gymId);
		return GymMapper.toDetailResponse(gym);
	}

	public List<TrainerDetailResponse> getTrainerDetail(Long gymId) {
		List<Trainer> trainers = trainerRepository.findByGym_GymId(gymId);
		List<Long> trainerIds = trainers.stream()
			.map(Trainer::getTrainerId)
			.toList();

		List<Award> awards = awardRepository.findByTrainerIdIn(trainerIds);
		List<PtProduct> ptProducts = ptProductRepository.findByTrainerIdIn(trainerIds);

		//트레이너별로 묶기
		Map<Long, List<Award>> awardMap = awards.stream()
			.collect(Collectors.groupingBy(Award::getTrainerId));

		Map<Long, List<PtProduct>> productMap = ptProducts.stream()
			.collect(Collectors.groupingBy(PtProduct::getTrainerId));

		return trainers.stream()
			.map(trainer -> trainerMapper.toTrainerDetailResponse(
				trainer,
				awardMap.getOrDefault(trainer.getTrainerId(), List.of()),
				productMap.getOrDefault(trainer.getTrainerId(), List.of())
			))
			.toList();
	}
}
