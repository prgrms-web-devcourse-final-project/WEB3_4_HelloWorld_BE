package org.helloworld.gymmate.domain.pt.classtime.mapper;

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

}
