package org.helloworld.gymmate.domain.gym.gym.repository;

import org.helloworld.gymmate.domain.gym.gym.entity.PartnerGym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerGymRepository extends JpaRepository<PartnerGym, Long> {

	boolean existsByOwnerIdAndGym_GymId(Long ownerId, Long gymId);

}
