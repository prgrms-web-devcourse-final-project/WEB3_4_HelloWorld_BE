package org.helloworld.gymmate.domain.user.trainer.business.dto.request;

import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.domain.user.trainer.dto.OwnerRegisterRequest;

public record BusinessVerificationRequest(List<Map<String, String>> businesses) {
	public static BusinessVerificationRequest from(OwnerRegisterRequest registerRequest) {
		return new BusinessVerificationRequest(List.of(Map.of(
			"b_no", registerRequest.businessNumber(),
			"start_dt", registerRequest.businessDate(),
			"p_nm", registerRequest.trainerName()
		)));
	}
}
