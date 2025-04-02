package org.helloworld.gymmate.domain.gym.gymInfo.dto.request;

import java.util.List;

public record UpdateGymRequest(
	GymInfoRequest gymInfoRequest,
	List<Long> deleteImageIds
) {
}
