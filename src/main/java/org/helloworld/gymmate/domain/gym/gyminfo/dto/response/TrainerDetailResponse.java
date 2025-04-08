package org.helloworld.gymmate.domain.gym.gyminfo.dto.response;

import java.util.List;

public record TrainerDetailResponse(
	String trainerName,
	String intro,
	String field,
	String career,
	String profileUrl,
	List<AwardResponse> awards,
	List<PtProductResponse> ptProducts
) {

	public record AwardResponse(
		String awardYear,
		String awardName,
		String awardInfo
	) {
	}

	public record PtProductResponse(
		String ptProductName,
		int ptProductFee
	) {
	}

}
