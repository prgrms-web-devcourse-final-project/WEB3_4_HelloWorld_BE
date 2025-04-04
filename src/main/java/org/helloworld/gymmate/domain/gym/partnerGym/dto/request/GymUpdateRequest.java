package org.helloworld.gymmate.domain.gym.partnerGym.dto.request;

import java.util.List;

public record GymUpdateRequest(
	GymInfoRequest gymInfoRequest,
	List<Long> deleteImageIds
) {
}
