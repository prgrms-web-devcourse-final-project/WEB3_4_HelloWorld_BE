package org.helloworld.gymmate.domain.pt.pt_product.enums;

import java.util.Arrays;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;

public enum PtProductSortOption {
	// TODO : 나중에 가격순 추가 가능
	LATEST, SCORE, NEARBY;

	public static PtProductSortOption from(String value) {
		return Arrays.stream(values())
			.filter(option -> option.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCode.UNSUPPORTED_SORT_OPTION));
	}
}