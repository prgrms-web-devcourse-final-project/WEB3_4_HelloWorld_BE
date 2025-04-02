package org.helloworld.gymmate.common.util;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;

public class PageUtil {
	public static void validatePageParams(int page, int pageSize) {
		if (page < 0) {
			throw new BusinessException(ErrorCode.NEGATIVE_PAGE_NOT_ALLOWED);
		}
		if (pageSize < 1 || pageSize > 50) {
			throw new BusinessException(ErrorCode.PAGE_SIZE_NOT_ALLOWED);
		}
	}
}
