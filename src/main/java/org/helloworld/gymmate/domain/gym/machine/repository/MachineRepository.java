package org.helloworld.gymmate.domain.gym.machine.repository;

import org.helloworld.gymmate.domain.gym.machine.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine, Long> {
}
