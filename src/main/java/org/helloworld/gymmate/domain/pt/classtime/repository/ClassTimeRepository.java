package org.helloworld.gymmate.domain.pt.classtime.repository;

import java.util.Optional;

import org.helloworld.gymmate.domain.pt.classtime.entity.ClassTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassTimeRepository extends JpaRepository<ClassTime, Long> {
	Optional<ClassTime> findByTrainerIdAndDayOfWeekAndTime(Long trainerId, Integer dayOfWeek, Integer time);
}
