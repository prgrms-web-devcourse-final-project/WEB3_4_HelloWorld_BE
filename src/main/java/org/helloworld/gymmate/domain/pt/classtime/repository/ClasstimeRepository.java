package org.helloworld.gymmate.domain.pt.classtime.repository;

import java.util.Optional;

import java.util.List;

import org.helloworld.gymmate.domain.pt.classtime.entity.Classtime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClasstimeRepository extends JpaRepository<Classtime, Long> {
	Optional<Classtime> findByTrainerIdAndDayOfWeekAndTime(Long trainerId, Integer dayOfWeek, Integer time);
	List<Classtime> findByTrainerId(Long trainerId);
}
