package org.helloworld.gymmate.domain.user.trainer.enums;

import java.util.Arrays;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;

public enum TrainerSearchOption {
	NONE, TRAINER, DISTRICT;

	public static TrainerSearchOption from(String value) {
		if (value == null)
			return NONE;
		return Arrays.stream(values())
			.filter(option -> option.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCode.UNSUPPORTED_SEARCH_OPTION));
	}
}