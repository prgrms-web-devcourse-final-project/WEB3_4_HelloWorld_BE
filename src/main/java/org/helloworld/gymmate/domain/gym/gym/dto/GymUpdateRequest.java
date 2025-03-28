package org.helloworld.gymmate.domain.gym.gym.dto;

import java.util.List;

public record GymUpdateRequest(
	String gymName,
	String startTime,
	String endTime,
	String phoneNumber,
	String address,
	String xField,
	String yField,
	String intro,
	List<String> existingImageUrls
) {
}
