package org.helloworld.gymmate.domain.user.trainer.enums;

import java.util.Arrays;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;

public enum TrainerSortOption {
	LATEST, SCORE, NEARBY;

	public static TrainerSortOption from(String value) {
		return Arrays.stream(values())
			.filter(option -> option.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCode.UNSUPPORTED_SORT_OPTION));
	}
}