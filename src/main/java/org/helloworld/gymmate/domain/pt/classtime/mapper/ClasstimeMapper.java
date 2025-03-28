package org.helloworld.gymmate.domain.pt.classtime.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.helloworld.gymmate.domain.pt.classtime.dto.ClasstimeRequest;
import org.helloworld.gymmate.domain.pt.classtime.entity.Classtime;

public class ClasstimeMapper {

	public static Classtime toEntity(ClasstimeRequest request, Long trainerId){
		return Classtime.builder()
			.time(request.time())
			.dayOfWeek(request.dayOfWeek())
			.trainerId(trainerId)
			.build();
	}

	public static Map<Integer, List<Integer>> toClasstimesResponse(
		List<Classtime> classtimes
	){
		return classtimes.stream()
			.collect(Collectors.groupingBy(
				Classtime::getDayOfWeek,
				Collectors.mapping(Classtime::getTime, Collectors.toList())
			));
	}
}
