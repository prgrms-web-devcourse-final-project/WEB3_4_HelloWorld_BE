package org.helloworld.gymmate.domain.pt.classtime.dto;

import java.util.List;
import java.util.Map;

public record ClasstimesResponse(
	Map<Integer, List<String>> availableTimes
) {
}
