package org.helloworld.gymmate.domain.gym.gyminfo.dto.response;

public record GymSearchResponse(
	String gymName,
	String address,
	String imageUrl
) {
}
