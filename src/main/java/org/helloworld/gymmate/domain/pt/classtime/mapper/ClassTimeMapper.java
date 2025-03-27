package org.helloworld.gymmate.domain.pt.classtime.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.helloworld.gymmate.domain.pt.classtime.dto.ClassTimeRequest;
import org.helloworld.gymmate.domain.pt.classtime.entity.ClassTime;

public class ClassTimeMapper {

	public static ClassTime toEntity(ClassTimeRequest request, Long trainerId){
		return ClassTime.builder()
			.time(request.time())
			.dayOfWeek(request.dayOfWeek())
			.trainerId(trainerId)
			.build();
	}

	public static Map<Integer, List<String>> toClassTimesResponse(
		List<ClassTime> classTimes
	){
		return classTimes.stream()
			.collect(Collectors.groupingBy(
				ClassTime::getDayOfWeek,
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
