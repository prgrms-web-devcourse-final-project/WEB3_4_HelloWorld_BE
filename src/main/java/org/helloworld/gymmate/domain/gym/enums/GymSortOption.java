package org.helloworld.gymmate.domain.gym.enums;

import java.util.Arrays;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;

public enum GymSortOption {
	SCORE, NEARBY;

	public static GymSortOption from(String value) {
		return Arrays.stream(values())
			.filter(option -> option.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCode.UNSUPPORTED_SORT_OPTION));
	}
}
