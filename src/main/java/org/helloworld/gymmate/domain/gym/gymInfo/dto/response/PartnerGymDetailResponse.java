package org.helloworld.gymmate.domain.gym.gymInfo.dto.response;

import java.util.List;

public record PartnerGymDetailResponse(
	Long partnerGymId,
	GymInfoResponse gymInfo,
	List<GymProductResponse> gymProducts,
	List<GymImageResponse> images
) {
}
