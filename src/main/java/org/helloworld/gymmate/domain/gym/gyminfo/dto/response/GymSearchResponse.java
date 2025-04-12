package org.helloworld.gymmate.domain.gym.gyminfo.dto.response;

public record GymSearchResponse(
    long gymId,
    String gymName,
    String address,
    String imageUrl
) {
}
