package org.helloworld.gymmate.domain.gym.gym.repository;

import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
}
