package org.helloworld.gymmate.common.mapper;

import org.springframework.data.domain.Page;
import org.helloworld.gymmate.common.dto.PageDto;

public class PageMapper {
	public static <T> PageDto<T> toPageDto(Page<T> page) {
		return new PageDto<>(
			page.getContent(),
			page.getNumber(),
			page.getTotalPages(),
			page.getTotalElements(),
			page.hasNext(),
			page.hasPrevious(),
			page.isLast()
		);
	}
}