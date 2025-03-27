package org.helloworld.gymmate.domain.pt.classtime.repository;

import java.util.List;

import org.helloworld.gymmate.domain.pt.classtime.entity.ClassTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassTimeRepository extends JpaRepository<ClassTime, Long> {
	List<ClassTime> findByTrainerId(Long trainerId);
}
