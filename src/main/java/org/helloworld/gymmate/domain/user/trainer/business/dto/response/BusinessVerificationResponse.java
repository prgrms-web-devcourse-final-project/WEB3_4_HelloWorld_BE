package org.helloworld.gymmate.domain.user.trainer.business.dto.response;

import java.util.List;
import java.util.Map;

public record BusinessVerificationResponse(
	List<Map<String, Object>> data
) {
}
