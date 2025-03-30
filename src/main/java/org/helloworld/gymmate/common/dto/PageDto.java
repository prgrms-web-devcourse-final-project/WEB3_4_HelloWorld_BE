package org.helloworld.gymmate.common.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PageDto<T>(
	List<T> content,
	int currentPage,
	int totalPages,
	long totalElements,
	boolean hasNext,
	boolean hasPrevious,
	@JsonProperty("isLast") boolean isLast
) {}
