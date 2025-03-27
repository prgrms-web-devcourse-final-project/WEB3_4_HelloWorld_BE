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

	public static Map<Integer, List<String>> toClasstimesResponse(
		List<Classtime> classtimes
	){
		return classtimes.stream()
			.collect(Collectors.groupingBy(
				Classtime::getDayOfWeek,
				Collectors.mapping(ct -> convertToTimeFormat(ct.getTime()), Collectors.toList())
			));
	}

	private static String convertToTimeFormat(Integer time) {
		return String.format("%02d:00", time); // 14 -> "14:00"
	}

	private static String convertToDayFormat(Integer time) {
		String[] days = {"일", "월", "화", "수", "목", "금", "토"};
		return days[time];
	}

}
