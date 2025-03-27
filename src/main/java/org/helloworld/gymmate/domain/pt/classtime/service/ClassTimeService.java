package org.helloworld.gymmate.domain.pt.classtime.service;

import java.util.List;

import org.helloworld.gymmate.domain.pt.classtime.dto.ClassTimeRequest;
import org.helloworld.gymmate.domain.pt.classtime.dto.ClassTimesResponse;
import org.helloworld.gymmate.domain.pt.classtime.entity.ClassTime;
import org.helloworld.gymmate.domain.pt.classtime.mapper.ClassTimeMapper;
import org.helloworld.gymmate.domain.pt.classtime.repository.ClassTimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassTimeService {
	private final ClassTimeRepository classTimeRepository;

	@Transactional
	public ClassTime createClassTime(@Valid ClassTimeRequest request) {
		// TODO : userDetail에서 id 가져와야 함
		Long trainerId = 0L;
		return classTimeRepository.save(
			ClassTimeMapper.toEntity(request, trainerId));
	}

	public ClassTimesResponse getAvailableTimes() {
		// TODO : userDetail에서 id 가져와야 함
		Long trainerId = 0L;
		List<ClassTime> classTimes = classTimeRepository.findByTrainerId(trainerId);
		return new ClassTimesResponse(ClassTimeMapper.toClassTimesResponse(classTimes));
	}
}
