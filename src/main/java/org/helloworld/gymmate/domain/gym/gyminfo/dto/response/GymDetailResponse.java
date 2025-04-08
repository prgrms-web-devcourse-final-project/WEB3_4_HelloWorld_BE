package org.helloworld.gymmate.domain.gym.gyminfo.dto.response;

import java.util.List;

import org.helloworld.gymmate.domain.gym.partnergym.dto.response.GymProductResponse;

public record GymDetailResponse(
    Long gymId,
    String gymName,
    String startTime,
    String endTime,
    String phoneNumber,
    String address,
    String xField,
    String yField,
    Double avgScore,
    String intro,
    Boolean isPartner,
    List<String> gymImages,
    List<GymProductResponse> gymProductResponses
) {
}
