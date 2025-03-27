package org.helloworld.gymmate.domain.pt.classtime.service;

import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
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
		if( classTimeRepository.findByTrainerIdAndDayOfWeekAndTime(trainerId,request.dayOfWeek(),request.time()).isPresent()){
			throw new BusinessException(ErrorCode.CLASSTIME_DUPLICATED);
		}
		return classTimeRepository.save(
			ClassTimeMapper.toEntity(request, trainerId));
	}

	public void deleteClassTime(Integer dayOfWeek, Integer time) {
		// TODO : userDetail에서 id 가져와야 함
		Long trainerId = 0L;
		ClassTime classTime = classTimeRepository.findByTrainerIdAndDayOfWeekAndTime(trainerId,dayOfWeek,time)
			.orElseThrow(() -> new BusinessException(ErrorCode.CLASSTIME_NOT_FOUND));
		classTimeRepository.delete(classTime);
	}

	public ClassTimesResponse getAvailableTimes() {
		// TODO : userDetail에서 id 가져와야 함
		Long trainerId = 0L;
		List<ClassTime> classTimes = classTimeRepository.findByTrainerId(trainerId);
		return new ClassTimesResponse(ClassTimeMapper.toClassTimesResponse(classTimes));
	}
}
