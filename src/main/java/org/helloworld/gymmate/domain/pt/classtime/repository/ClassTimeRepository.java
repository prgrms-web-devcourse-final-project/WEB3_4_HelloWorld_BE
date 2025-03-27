package org.helloworld.gymmate.domain.pt.classtime.repository;

import org.helloworld.gymmate.domain.pt.classtime.entity.ClassTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassTimeRepository extends JpaRepository<ClassTime, Long> {
}
