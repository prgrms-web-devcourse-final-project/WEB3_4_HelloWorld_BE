package org.helloworld.gymmate.domain.gym.machine.repository;

import org.helloworld.gymmate.domain.gym.machine.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MachineRepository extends JpaRepository<Machine, Long> {
	@Query("SELECT COUNT(m) FROM Machine m WHERE m.gym.id = :gymId")
	long countByGymId(@Param("gymId") Long gymId);
}
