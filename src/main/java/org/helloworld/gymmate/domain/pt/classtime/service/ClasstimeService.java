package org.helloworld.gymmate.domain.pt.classtime.service;

import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.pt.classtime.dto.ClasstimeRequest;
import org.helloworld.gymmate.domain.pt.classtime.dto.ClasstimesResponse;
import org.helloworld.gymmate.domain.pt.classtime.entity.Classtime;
import org.helloworld.gymmate.domain.pt.classtime.mapper.ClasstimeMapper;
import org.helloworld.gymmate.domain.pt.classtime.repository.ClasstimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClasstimeService {
    private final ClasstimeRepository classtimeRepository;

    @Transactional
    public Classtime createClasstime(@Valid ClasstimeRequest request, Long trainerId) {
        if (classtimeRepository.findByTrainerIdAndDayOfWeekAndTime(trainerId, request.dayOfWeek(), request.time())
            .isPresent()) {
            throw new BusinessException(ErrorCode.CLASSTIME_DUPLICATED);
        }
        return classtimeRepository.save(
            ClasstimeMapper.toEntity(request, trainerId));
    }

    public void deleteClasstime(Integer dayOfWeek, Integer time, Long trainerId) {
        Classtime classtime = classtimeRepository.findByTrainerIdAndDayOfWeekAndTime(trainerId, dayOfWeek, time)
            .orElseThrow(() -> new BusinessException(ErrorCode.CLASSTIME_NOT_FOUND));
        classtimeRepository.delete(classtime);
    }

    public ClasstimesResponse getAvailableTimes(Long trainerId) {
        List<Classtime> classtimes = classtimeRepository.findByTrainerId(trainerId);
        return new ClasstimesResponse(ClasstimeMapper.toClasstimesResponse(classtimes));
    }
}
