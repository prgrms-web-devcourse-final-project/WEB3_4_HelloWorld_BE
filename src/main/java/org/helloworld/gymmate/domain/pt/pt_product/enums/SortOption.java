package org.helloworld.gymmate.domain.pt.pt_product.enums;

import java.util.Arrays;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;

public enum SortOption {
	LATEST, SCORE, NEAREST;

	public static SortOption from(String value) {
		return Arrays.stream(values())
			.filter(option -> option.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCode.UNSUPPORTED_SORT_OPTION));
	}
}