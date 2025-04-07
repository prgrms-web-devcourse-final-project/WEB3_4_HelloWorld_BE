package org.helloworld.gymmate.domain.gym.partnergym.mapper;

import java.util.List;

import org.helloworld.gymmate.domain.gym.facility.mapper.FacilityMapper;
import org.helloworld.gymmate.domain.gym.gyminfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gyminfo.entity.GymImage;
import org.helloworld.gymmate.domain.gym.gymproduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.partnergym.dto.response.GymImageResponse;
import org.helloworld.gymmate.domain.gym.partnergym.dto.response.GymInfoResponse;
import org.helloworld.gymmate.domain.gym.partnergym.dto.response.GymProductResponse;
import org.helloworld.gymmate.domain.gym.partnergym.dto.response.PartnerGymDetailResponse;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;

public class PartnerGymMapper {

	public static PartnerGymDetailResponse toDto(PartnerGym partnerGym) {
		Gym gym = partnerGym.getGym();

		List<GymProductResponse> gymProductResponses = partnerGym.getGymProducts().stream()
			.map(PartnerGymMapper::toGymProductDto)
			.toList();

		List<GymImageResponse> gymImageResponses = gym.getImages().stream()
			.map(PartnerGymMapper::toDto)
			.toList();

		return new PartnerGymDetailResponse(
			partnerGym.getPartnerGymId(),
			toGymInfoDto(gym),
			gymProductResponses,
			gymImageResponses
		);

	}

	private static GymInfoResponse toGymInfoDto(Gym gym) {
		return new GymInfoResponse(
			gym.getStartTime(),
			gym.getEndTime(),
			gym.getIntro(),
			FacilityMapper.toDto(gym.getFacility())
		);
	}

	private static GymProductResponse toGymProductDto(GymProduct product) {
		return new GymProductResponse(
			product.getGymProductId(),
			product.getGymProductMonth(),
			product.getGymProductFee()
		);
	}

	public static GymImageResponse toDto(GymImage gymImage) {
		return new GymImageResponse(
			gymImage.getGymImageId(),
			gymImage.getUrl()
		);
	}
}
