package org.helloworld.gymmate.domain.gym.enums;

import java.util.Arrays;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;

public enum GymSearchOption {
	NONE, GYM, DISTRICT;

	public static GymSearchOption from(String value) {
		if (value == null)
			return NONE;
		return Arrays.stream(values())
			.filter(option -> option.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCode.UNSUPPORTED_SEARCH_OPTION));
	}
}
